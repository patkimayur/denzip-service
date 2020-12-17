package com.crs.denzip.core.config;

import com.crs.denzip.core.controller.CRSController;
import com.crs.denzip.core.pool.AsyncExecutorPool;
import com.crs.denzip.core.scheduler.ListingNotificationScheduler;
import com.crs.denzip.core.scheduler.ListingStatusUpdateScheduler;
import com.crs.denzip.core.service.ApartmentService;
import com.crs.denzip.core.service.BrokerOrgService;
import com.crs.denzip.core.service.DeactivationService;
import com.crs.denzip.core.service.FileHandlingService;
import com.crs.denzip.core.service.FilterService;
import com.crs.denzip.core.service.GoogleService;
import com.crs.denzip.core.service.ListingService;
import com.crs.denzip.core.service.NotificationService;
import com.crs.denzip.core.service.RazorpayService;
import com.crs.denzip.core.service.StorageService;
import com.crs.denzip.core.service.TagService;
import com.crs.denzip.core.service.UserService;
import com.crs.denzip.model.config.ModelConfig;
import com.crs.denzip.model.marshaller.AmenitiesMarshaller;
import com.crs.denzip.model.marshaller.CRSImageMarshaller;
import com.crs.denzip.model.marshaller.CRSMarshaller;
import com.crs.denzip.model.marshaller.LocalityDataMarshaller;
import com.crs.denzip.model.marshaller.UserPrefVisitSlotMarshaller;
import com.crs.denzip.persistence.annotation.audit.AuditAspect;
import com.crs.denzip.persistence.annotation.monitor.MonitorTimeAspect;
import com.crs.denzip.persistence.dao.ApartmentDAO;
import com.crs.denzip.persistence.dao.AuditDAO;
import com.crs.denzip.persistence.dao.BrokerOrgDAO;
import com.crs.denzip.persistence.dao.DeactivationDAO;
import com.crs.denzip.persistence.dao.FiltersDAO;
import com.crs.denzip.persistence.dao.ListingDAO;
import com.crs.denzip.persistence.dao.NotificationDAO;
import com.crs.denzip.persistence.dao.RazorpayOrderDAO;
import com.crs.denzip.persistence.dao.TagDAO;
import com.crs.denzip.persistence.dao.UserDAO;
import com.crs.denzip.security.config.AuthServerOAuth2Config;
import com.crs.denzip.security.config.ConnectorConfig;
import com.crs.denzip.security.config.ResourceServerConfig;
import com.crs.denzip.security.dummyconfig.WebSecurityConfig;
import com.crs.denzip.security.otpgen.TimeBasedOneTimePasswordGenerator;
import com.crs.denzip.security.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableSwagger2
@EnableAutoConfiguration
@Configuration
@Import({ModelConfig.class, AuthServerOAuth2Config.class, ResourceServerConfig.class, WebSecurityConfig.class, ConnectorConfig.class})
public class CRSConfig {

  @Autowired
  private CRSMarshaller crsMarshaller;

  @Autowired
  private LocalityDataMarshaller localityDataMarshaller;

  @Autowired
  private UserPrefVisitSlotMarshaller userPrefVisitSlotMarshaller;

  @Autowired
  private AmenitiesMarshaller amenitiesMarshaller;

  @Autowired
  private CRSImageMarshaller crsImageMarshaller;

  @Autowired
  private JavaMailSender emailSender;

  @Value("${image.upload.dir}")
  private String imageUploadDir;

  @Value("${deactivationValidDays}")
  private String deactivationValidDays;

  @Value("${google.api.key}")
  private String googleAPIKey;

  @Value("${google.place.types}")
  private String googlePlaceTypes;

  @Value("${google.locality.search.radius}")
  private int searchRadius;

  @Value("${sms.api.key}")
  private String smsAPIKey;

  @Value("${sms.message}")
  private String smsMessage;

  @Value("${mail.subjectForScheduling}")
  private String mailSubject;

  @Value("${mail.textForScheduling}")
  private String mailText;

  @Value("${mail.subjectForRequestCallback}")
  private String mailSubjectForRequestCallback;

  @Value("${mail.subjectForScheduleVisit}")
  private String mailSubjectForScheduleVisit;


  @Value("${mail.textForRequestCallback}")
  private String mailTextForRequestCallback;

  @Value("${mail.textForScheduleVisit}")
  private String mailTextForScheduleVisit;

  @Value("${sms.messageForSchedulingConfirmation}")
  private String smsMessageForSchedulingConfirmation;

  @Value("${sms.adminMessageForSchedulingConfirmation}")
  private String smsAdminMessageForSchedulingConfirmation;

  @Value("${sms.smsMessageForRequestCallback}")
  private String smsMessageForRequestCallback;

  @Value("${sms.smsMessageForDenzipAdmin}")
  private String smsMessageForDenzipAdmin;

  @Value("#{'${sms.denzipAdminPhones}'.split(',')}")
  private List<String> denzipAdminPhones;

  @Value("#{'${mail.denzipAdminEmails}'.split(',')}")
  private List<String> denzipAdminEmails;

  @Value("${sms.test}")
  private boolean isSMSTest;

  @Value("${otp.generation.timeStep}")
  private int otpGenerationTimeStep;

  @Value("${asyncPool.size : 1}")
  private int poolSize;

  @Value("${zone.id}")
  private String zoneId;

  @Value("${listingStatusUpdateScheduler.recurring.period.hour}")
  private int recurringPeriodInHour;

  @Value("${listing.deactivation.interval}")
  private String deactivationInterval;

  @Value("${listing.deactivation.sms}")
  private String deactivationSms;

  @Value("${listing.deactivation.subject}")
  private String deactivationSubject;

  @Value("${listing.deactivation.message}")
  private String deactivationMessage;

  @Value("${listing.deactivation.warning.interval}")
  private String deactivationWarningInterval;

  @Value("${listing.deactivation.warning.sms}")
  private String deactivationWarningSms;

  @Value("${listing.deactivation.warning.subject}")
  private String deactivationWarningSubject;

  @Value("${listing.deactivation.warning.message}")
  private String deactivationWarningMessage;

  @Value("${notification.initial.delay.seconds}")
  private int initialDelay;

  @Value("${listing.notification.frequency.hour}")
  private int listingNotificationFrequency;

  @Value("${broker.notification.frequency.hour}")
  private int brokerNotificationFrequency;

  @Value("${server.address}")
  private String serverAddress;

  @Value("${server.port}")
  private String serverPort;

  @Value("${listing.recentlyAddedDays}")
  private String recentlyAddedDays;

  @Value("${razorpay.key.id}")
  private String razorpayKeyId;

  @Value("${razorpay.key.secret}")
  private String razorpayKeySecret;


  @Bean
  public AsyncExecutorPool asyncExecutorPool() {
    return new AsyncExecutorPool(poolSize);
  }


  @Bean
  public ListingDAO listingDAO() {
    return new ListingDAO(amenitiesMarshaller, localityDataMarshaller, crsImageMarshaller, zoneId);
  }


  @Bean
  public UserDAO userDAO() {
    return new UserDAO(userPrefVisitSlotMarshaller);
  }

  @Bean
  public ApartmentDAO apartmentDAO() {
    return new ApartmentDAO(amenitiesMarshaller, crsImageMarshaller);
  }

  @Bean
  public AuditDAO auditDAO() {
    return new AuditDAO(crsMarshaller);
  }

  @Bean
  public DeactivationDAO deactivationDAO() {
    return new DeactivationDAO(deactivationInterval, deactivationWarningInterval);
  }

  @Bean
  public TagDAO tagDAO() {
    return new TagDAO();
  }

  @Bean
  public NotificationDAO notificationDAO() {
    return new NotificationDAO();
  }

  @Bean
  public BrokerOrgDAO brokerOrgDAO() {
    return new BrokerOrgDAO();
  }

  @Bean
  public AuditAspect auditAspect() {
    return new AuditAspect(auditDAO());
  }

  @Bean
  public MonitorTimeAspect monitorTimeAspect() {
    return new MonitorTimeAspect();
  }



  @Bean
  public FilterService filterService() {
    return new FilterService(filtersDAO());
  }

  @Bean
  public FiltersDAO filtersDAO() {
    return new FiltersDAO();
  }

  @Bean
  public RazorpayOrderDAO razorpayOrderDAO() {
    return new RazorpayOrderDAO();
  }


  @Bean
  public GoogleService googleService() {
    return new GoogleService(this.googleAPIKey, this.googlePlaceTypes, this.searchRadius);
  }

  @Bean
  public ListingService listingService() {
    return new ListingService(listingDAO(), googleService(), tagService(), deactivationValidDays, zoneId, recentlyAddedDays);
  }

  @Bean
  public TimeBasedOneTimePasswordGenerator timeBasedOneTimePasswordGenerator() throws NoSuchAlgorithmException {
    return new TimeBasedOneTimePasswordGenerator(otpGenerationTimeStep, TimeUnit.SECONDS);
  }

  @Bean
  public UserService userService() throws Exception {
    return new UserService(userDAO(), listingService(), asyncExecutorPool(), this.smsAPIKey, this.smsMessage, this.smsMessageForSchedulingConfirmation, this.smsMessageForRequestCallback, this.smsMessageForDenzipAdmin, this.smsAdminMessageForSchedulingConfirmation, this.mailSubjectForScheduleVisit,this.mailTextForScheduleVisit,
        this.denzipAdminPhones, this.denzipAdminEmails, this.isSMSTest, timeBasedOneTimePasswordGenerator(), this.emailSender, this.mailSubject, this.mailText, this.mailSubjectForRequestCallback, this.mailTextForRequestCallback);
  }

  @Bean
  public UserSecurityService userSecurityService() {
    return new UserSecurityService(userDAO());
  }

  @Bean
  public RazorpayService razorpayService() throws Exception {
    return new RazorpayService(razorpayKeyId, razorpayKeySecret, razorpayOrderDAO(), userService());
  }


  @Bean
  public CRSController crsController() throws Exception {
    return new CRSController(listingService(), filterService(), userService(), apartmentService(), fileHandlingService(), tagService(), notificationService(), brokerOrgService(), razorpayService(), crsMarshaller);
  }

  @Bean
  public ApartmentService apartmentService() {
    return new ApartmentService(apartmentDAO());
  }

  @Bean
  public StorageService storageService() {
    return new StorageService(imageUploadDir, listingDAO(), apartmentDAO());
  }

  @Bean
  public FileHandlingService fileHandlingService() {
    return new FileHandlingService(storageService());
  }

  @Bean
  public DeactivationService deactivationService() throws Exception {
    return new DeactivationService(deactivationDAO(), userService(), deactivationInterval, deactivationSms, deactivationSubject, deactivationMessage, deactivationWarningInterval, deactivationWarningSms, deactivationWarningSubject, deactivationWarningMessage);
  }

  @Bean
  public ListingStatusUpdateScheduler listingStatusUpdateScheduler() throws Exception {
    return new ListingStatusUpdateScheduler(zoneId, recurringPeriodInHour, deactivationService());
  }

  @Bean
  public ListingNotificationScheduler listingNotificationScheduler() throws Exception{
    return new ListingNotificationScheduler(notificationService(), initialDelay, listingNotificationFrequency, brokerNotificationFrequency);
  }

  @Bean
  public TagService tagService() {
    return new TagService(googleService(), tagDAO());
  }

  @Bean
  public NotificationService notificationService() throws Exception{
    return new NotificationService(userService(), notificationDAO());
  }

  @Bean
  public BrokerOrgService brokerOrgService() throws Exception{
    return new BrokerOrgService(userService(), brokerOrgDAO());
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("com.crs.denzip.core.controller"))
        .paths(PathSelectors.any())
        .build()
        .securitySchemes(Arrays.asList(securityScheme()))
        .securityContexts(Arrays.asList(securityContext()));
  }

  @Bean
  public SecurityConfiguration security() {
    return SecurityConfigurationBuilder.builder()
        .clientId("rw-client")
        .clientSecret("crs-rw-ro7&")
        .scopeSeparator(" ")
        .useBasicAuthenticationWithAccessCodeGrant(true)
        .build();
  }

  private SecurityScheme securityScheme() {
    GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("https://" + serverAddress + ":" + serverPort + "/crs" + "/oauth/token");

    SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
        .grantTypes(Arrays.asList(grantType))
        .scopes(Arrays.asList(scopes()))
        .build();
    return oauth;
  }

  private AuthorizationScope[] scopes() {
    AuthorizationScope[] scopes = {};
    //for now we are not using scopes, so will add these when required.
//    {
//        new AuthorizationScope("read", "for read operations"),
//        new AuthorizationScope("write", "for write operations")};
    return scopes;
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(
            Arrays.asList(new SecurityReference("spring_oauth", scopes())))
        .forPaths(PathSelectors.any())
        .build();
  }

}
