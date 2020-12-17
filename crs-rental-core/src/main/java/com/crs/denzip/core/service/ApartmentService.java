package com.crs.denzip.core.service;

import com.crs.denzip.model.exception.CRSException;
import com.crs.denzip.persistence.dao.ApartmentDAO;
import com.crs.denzip.model.entities.Apartment;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Transactional(rollbackFor={CRSException.class})
public class ApartmentService {

  private ApartmentDAO apartmentDAO;

  public List<Apartment> getAllApartments() {

    return apartmentDAO.getAllApartments();
  }

  public Apartment getApartment(String apartmentId) {

    return apartmentDAO.getApartment(apartmentId);
  }

  public Map<String, Boolean> getDefaultApartmentAmenities() {
    return apartmentDAO.getDefaultApartmentAmenities();
  }

  public String addApartment(Apartment apartment) {
    return apartmentDAO.addApartment(apartment);
  }

  public String updateApartment(Apartment apartment) {
    return apartmentDAO.updateApartment(apartment);
  }

}
