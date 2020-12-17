package com.crs.denzip.core.service;

import com.crs.denzip.persistence.annotation.monitor.MonitorTime;
import com.crs.denzip.model.entities.CRSImage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
public class FileHandlingService {

  private final StorageService storageService;

  public List<CRSImage> uploadListingImages(String listingId, MultipartFile[] files) {
    return this.storageService.storeListingImages(listingId, files);
  }

  public List<CRSImage> uploadApartmentImages(String apartmentId, MultipartFile[] files) {
    return this.storageService.storeApartmentImages(apartmentId, files);
  }

  @MonitorTime(category = "FileHandlingService", subCategory = "loadListingImage")
  public Resource loadListingImage(String listingId, String imageId) {
    return this.storageService.loadListingImage(listingId, imageId);
  }

  @MonitorTime(category = "FileHandlingService", subCategory = "loadApartmentImage")
  public Resource loadApartmentImage(String apartmentId, String imageId) {
    return this.storageService.loadApartmentImage(apartmentId, imageId);
  }

}
