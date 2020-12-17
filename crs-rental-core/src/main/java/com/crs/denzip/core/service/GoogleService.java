package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.LocalityCategory;
import com.crs.denzip.model.entities.LocalityData;
import com.crs.denzip.model.entities.LocalityResource;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GoogleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleService.class);

  private final String googleAPIKey;
  private final String googlePlaceTypes;

  private GeoApiContext geoApiContext;
  private List<PlaceType> placeTypeList;
  private final int searchRadius;


  @PostConstruct
  public void init() {
    geoApiContext = new GeoApiContext.Builder().apiKey(this.googleAPIKey)
                                               .build();
    placeTypeList = Arrays.stream(googlePlaceTypes.split(","))
                          .map(this::fromPlaceTypeValue)
                          .filter(Objects::nonNull)
                          .collect(Collectors.toList());
  }

  private PlaceType fromPlaceTypeValue(String placeTypeValue) {
    for (PlaceType placeType : PlaceType.values()) {
      if (Objects.equals(placeType.toUrlValue(), placeTypeValue)) {
        return placeType;
      }
    }
    return null;
  }

  public String getSubLocality(double latitude, double longitude) {
    LatLng latLng = new LatLng(latitude, longitude);

    try {
      GeocodingApiRequest geocodingApiRequest = GeocodingApi.reverseGeocode(geoApiContext, latLng);
      GeocodingResult[] geocodingResults = geocodingApiRequest.await();
      for (GeocodingResult geocodingResult : geocodingResults) {
        for(AddressComponent addressComponent : geocodingResult.addressComponents) {
          for(AddressComponentType addressComponentType : addressComponent.types) {
            if(AddressComponentType.SUBLOCALITY_LEVEL_1 == addressComponentType) {
              return addressComponent.longName;
            }
          }
        }
      }

    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    return null;
  }

  public LocalityData getListingLocalityData(double latitude, double longitude) {
    LatLng latLng = new LatLng(latitude, longitude);
    LocalityData localityData = new LocalityData(new ArrayList<>());

    for (PlaceType placeType : placeTypeList) {
      LocalityCategory localityCategory = new LocalityCategory(getNormalizedPlaceTypeName(placeType), new ArrayList<>());
      localityData.getCategories()
                  .add(localityCategory);

      // Synchronous
      try {
        PlacesSearchResult[] placesSearchResults = getPlacesSearchResponse(latLng, placeType);
        for (PlacesSearchResult placesSearchResult : placesSearchResults) {

          // Only proceed if name is in english or latin characters
          if (onlyEnglish(placesSearchResult.name)) {
            double lat = placesSearchResult.geometry.location.lat;
            double lng = placesSearchResult.geometry.location.lng;
            DistanceMatrixElement distanceMatrixElement = getDistanceMatrix(latLng, new LatLng(lat, lng));

            LocalityResource localityResource = LocalityResource.builder()
                                                                .name(placesSearchResult.name)
                                                                .latitude(lat)
                                                                .longitude(lng)
                                                                .distance(distanceMatrixElement.distance.humanReadable)
                                                                .duration(distanceMatrixElement.duration.humanReadable)
                                                                .build();
            localityCategory.getResources()
                            .add(localityResource);

            if (localityCategory.getResources()
                                .size() >= 5) {
              break;
            }
          }
          else {
            LOGGER.info(placesSearchResult.name);
          }
        }
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
      }
    }

    return localityData;
  }

  private String getNormalizedPlaceTypeName(PlaceType placeType) {
    String placeTypeName = placeType.name();
    return placeTypeName.split("_")[0];
  }

  private PlacesSearchResult[] getPlacesSearchResponse(LatLng latLng, PlaceType placeType) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException {
    NearbySearchRequest nearbySearchRequest = PlacesApi.nearbySearchQuery(geoApiContext, latLng)
                                                       .radius(searchRadius)
                                                       .rankby(RankBy.PROMINENCE)
                                                       .type(placeType);
    PlacesSearchResponse placesSearchResponse = nearbySearchRequest.await();
    LOGGER.debug(placesSearchResponse.toString());
    return placesSearchResponse.results;
  }

  private DistanceMatrixElement getDistanceMatrix(LatLng originLatLang, LatLng destinationLatLang) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException {
    DistanceMatrixApiRequest distanceMatrixApiRequest = DistanceMatrixApi.newRequest(geoApiContext)
                                                                         .origins(originLatLang)
                                                                         .destinations(destinationLatLang)
                                                                         .mode(TravelMode.DRIVING)
                                                                         .units(Unit.METRIC);
    DistanceMatrix distanceMatrix = distanceMatrixApiRequest.await();
    LOGGER.debug(distanceMatrix.toString());
    return distanceMatrix.rows[0].elements[0];
  }

  private boolean onlyEnglish(String text) {

    for (char character : text.toCharArray()) {

      if (Character.UnicodeBlock.of(character) == Character.UnicodeBlock.BASIC_LATIN
          || Character.UnicodeBlock.of(character) == Character.UnicodeBlock.LATIN_1_SUPPLEMENT
          || Character.UnicodeBlock.of(character) == Character.UnicodeBlock.LATIN_EXTENDED_A
          || Character.UnicodeBlock.of(character) == Character.UnicodeBlock.GENERAL_PUNCTUATION) {

      }
      else {
        return false;
      }
    }

    return true;
  }

}
