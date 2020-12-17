package com.crs.denzip.core.service;

import com.crs.denzip.model.entities.CRSImage;
import com.crs.denzip.model.exception.CRSException;
import com.crs.denzip.persistence.dao.ApartmentDAO;
import com.crs.denzip.persistence.dao.ListingDAO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = {CRSException.class})
public class StorageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

  private static final String IMAGES_PATH = "images";
  private static final String LISTING_IMAGES_DIR = "listing";
  private static final String APARTMENT_IMAGES_DIR = "apartment";
  private static final String LOCALITY_IMAGES_DIR = "locality";


  private final Path rootListingLocation;
  private final Path rootApartmentLocation;
  private final Path rootLocalityLocation;

  private final ListingDAO listingDAO;
  private final ApartmentDAO apartmentDAO;

  public StorageService(String imageUploadDir, ListingDAO listingDAO, ApartmentDAO apartmentDAO) {
    this.rootListingLocation = Paths.get(imageUploadDir)
                                    .resolve(LISTING_IMAGES_DIR);
    this.rootApartmentLocation = Paths.get(imageUploadDir)
                                      .resolve(APARTMENT_IMAGES_DIR);
    this.rootLocalityLocation = Paths.get(imageUploadDir)
                                     .resolve(LOCALITY_IMAGES_DIR);

    this.listingDAO = listingDAO;
    this.apartmentDAO = apartmentDAO;
  }


  public List<CRSImage> storeListingImages(String listingId, MultipartFile[] files) {
    Path listingDirPath = this.rootListingLocation.resolve(listingId);
    List<CRSImage> crsImageList = storeImages(listingDirPath, files);

    listingDAO.updateListingImages(listingId, crsImageList);
    return crsImageList;
  }

  public List<CRSImage> storeApartmentImages(String apartmentId, MultipartFile[] files) {
    Path apartmentDirPath = this.rootApartmentLocation.resolve(apartmentId);
    List<CRSImage> crsImageList = storeImages(apartmentDirPath, files);

    apartmentDAO.updateApartmentImages(apartmentId, crsImageList);
    return crsImageList;
  }

  private List<CRSImage> storeImages(Path imagesDirPath, MultipartFile[] files) {
    createDir(imagesDirPath);
    List<CRSImage> crsImageList = new ArrayList<>(files.length);
    for (MultipartFile file : files) {
      LOGGER.debug("Started storing file: {}", file.getOriginalFilename());
      try {
        Path destinationFilePath = imagesDirPath.resolve(getRandomFileName(file.getOriginalFilename()));
        Files.copy(file.getInputStream(), destinationFilePath);

        String filePath = destinationFilePath.toAbsolutePath()
                                             .toString();
        int listingIndex = filePath.indexOf(IMAGES_PATH);
        String imagePath = filePath.substring(listingIndex);

        int[] widths = {400, 600, 800, 1000, 1200};
        String imageSet = resizeImage(file.getInputStream(), destinationFilePath.toAbsolutePath()
                                                                                .toString(), widths);

        CRSImage crsImage = CRSImage.builder()
                                    .defaultImage(imagePath)
                                    .imageSet(imageSet)
                                    .imageName(StringUtils.substringBeforeLast(file.getOriginalFilename(), "."))
                                    .build();
        crsImageList.add(crsImage);

      } catch (Exception e) {
        LOGGER.error("Error storing file: " + file.getOriginalFilename(), e);
      }
    }
    return crsImageList;
  }

  private String getRandomFileName(String originalFileName) {
    String extension = StringUtils.substringAfterLast(originalFileName, ".");
    return RandomStringUtils.randomAlphanumeric(6) + "." + extension;
  }

  public Resource loadListingImage(String listingId, String imageId) {
    Path file = rootListingLocation.resolve(listingId)
                                   .resolve(imageId);
    return getResource(file);
  }

  public Resource loadApartmentImage(String apartmentId, String imageId) {
    Path file = rootApartmentLocation.resolve(apartmentId)
                                     .resolve(imageId);
    return getResource(file);
  }

  private Resource getResource(Path file) {
    try {

      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      }
      else {
        LOGGER.error("Image does not exist!!");
      }
    } catch (MalformedURLException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return null;
  }

  @PostConstruct
  public void init() {
    createDir(this.rootListingLocation);
    createDir(this.rootApartmentLocation);
    createDir(this.rootLocalityLocation);
  }

  private void createDir(Path dirPath) {
    if (!dirPath.toFile()
                .exists()) {
      try {
        Files.createDirectory(dirPath);
      } catch (IOException e) {
        throw new RuntimeException("Could not initialize storage!", e);
      }
    }
  }


  private String resizeImage(InputStream originalImage, String destinationFile, int[] widths) throws IOException {
    long startTime = System.nanoTime();
    try {
      long startTime0 = System.nanoTime();
      BufferedImage image = ImageIO.read(originalImage);
      long endTime0 = System.nanoTime();
      LOGGER.debug("Total Time reading Image : " + (endTime0 - startTime0) / 1000000);

      StringBuilder imageSet = new StringBuilder();

      for (int finalWidth : widths) {
        long startTime1 = System.nanoTime();
        Image scaledImage = image.getScaledInstance(finalWidth, -1, Image.SCALE_SMOOTH);
        long endTime1 = System.nanoTime();
        LOGGER.debug("Total Time getScaledInstance : " + (endTime1 - startTime1) / 1000000);

        int width = scaledImage.getWidth(null);
        int height = scaledImage.getHeight(null);

        long startTime2 = System.nanoTime();
        // width and height are of the toolkit image
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        long endTime2 = System.nanoTime();
        LOGGER.debug("Total Time Graphics Draw : " + (endTime2 - startTime2) / 1000000);


        File scaledImageFile = new File(
            StringUtils.substringBeforeLast(destinationFile, ".") + "_" + finalWidth + "." + StringUtils.substringAfterLast(destinationFile, "."));

        long startTime3 = System.nanoTime();
        ImageIO.write(newImage, "jpg", scaledImageFile);
        long endTime3 = System.nanoTime();
        LOGGER.debug("Total Time writing Image : " + (endTime3 - startTime3) / 1000000);

        String filePath = scaledImageFile.getAbsolutePath();
        int listingIndex = filePath.indexOf(IMAGES_PATH);
        String imagePath = filePath.substring(listingIndex);


        imageSet.append(imagePath)
                .append(" ")
                .append(finalWidth)
                .append("w, ");
      }

      return StringUtils.removeEnd(imageSet.toString(), ", ");

    } finally {
      long endTime = System.nanoTime();
      LOGGER.debug("Total Time: " + (endTime - startTime) / 1000000);
    }

  }
}