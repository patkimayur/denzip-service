package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.entities.Tag;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagDAO extends AbstractDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(TagDAO.class);

  private static final String GET_TAG_ID = "select tag_id from tag where tag_name = :tag_name";

  private static final String ADD_TAG = "insert into tag (tag_name) values (:tag_name) returning tag_id";

  private static final String LISTING_TAG_UPDATE = "insert into listing_tag_mapping (listing_id, tag_id) values (:listing_id, :tag_id)  ON CONFLICT (listing_id) DO UPDATE SET tag_id = :tag_id";

  private static final String USER_TAG_UPDATE = "insert into user_tag_mapping (user_id, tag_id) values (:user_id, :tag_id)  ON CONFLICT (user_id, tag_id) DO NOTHING";

  private static final String USER_TAG_DELETE = "delete from user_tag_mapping where user_id = :user_id";

  private static final String GET_ALL_TAGS = "select tag_id, tag_name from tag";

  private static final String GET_USER_TAGS = "select tag.tag_id, tag.tag_name from tag, user_tag_mapping where user_tag_mapping.user_id = :user_id and user_tag_mapping.tag_id = tag.tag_id";


  public String updateListingTag(String listingId, String tagName) {
    String tagId = getTagId(tagName);

    if (StringUtils.isEmpty(tagId)) {
      // Tag does not exist, lets create it first
      LOGGER.debug("tagName {} does not exist, thus creating it", tagName);
      tagId = createTag(tagName);
      LOGGER.debug("tagName {} created with tagId {}", tagName, tagId);
    }

    if (StringUtils.isNotEmpty(tagId)) {
      LOGGER.debug("Going to map ListingId {} with tagId {}", listingId, tagId);
      MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
      paramSourceMap.addValue("listing_id", new SqlParameterValue(Types.OTHER, listingId));
      paramSourceMap.addValue("tag_id", new SqlParameterValue(Types.OTHER, tagId));

      int result = super.update(LISTING_TAG_UPDATE, paramSourceMap);
      if (result == 1) {
        LOGGER.debug("ListingId {} mapped to tagId {}", listingId, tagId);
        return tagName;
      }

    }

    return null;
  }

  private String getTagId(String tagName) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("tag_name", tagName);
    List<String> tagId = super.query(GET_TAG_ID, paramSourceMap, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return String.valueOf(rs.getObject("tag_id"));
      }
    });

    if (!CollectionUtils.isEmpty(tagId) && tagId.size() == 1) {
      return tagId.stream().findFirst().orElse(null);
    }

    return null;
  }

  private String createTag(String tagName) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("tag_name", tagName);

    KeyHolder keyHolder = new GeneratedKeyHolder();

    int result = super.update(ADD_TAG, paramSourceMap, keyHolder);

    LOGGER.debug("Keys: {}", keyHolder.getKeyList());

    if (result == 1) {
      return String.valueOf(keyHolder.getKeys()
                                     .get("tag_id"));
    }

    return null;

  }

  public boolean updateUserTags(String userId, List<String> tagIdList) {

    LOGGER.debug("Updating tags for user {} with tagIds {}", userId, tagIdList);

    if (CollectionUtils.isEmpty(tagIdList)) {
      return false;
    }

    // First drop existing tags
    this.dropUserTags(userId);

    List<Map<String, Object>> batchValues = new ArrayList<>(tagIdList.size());
    for (String tagId : tagIdList) {
      batchValues.add(
          new MapSqlParameterSource("user_id", new SqlParameterValue(Types.OTHER, userId))
              .addValue("tag_id", new SqlParameterValue(Types.OTHER, tagId))
              .getValues());
    }

    int[] updateCounts = super.batchUpdate(USER_TAG_UPDATE, batchValues.toArray(new Map[tagIdList.size()]));
    LOGGER.debug("Result of updating user {} with tags: {}", userId, updateCounts);

    if (ArrayUtils.isEmpty(updateCounts)) {
      return false;
    }

    if (tagIdList.size() == updateCounts.length) {
      return true;
    }

    return false;

  }

  private int dropUserTags(String userId) {
    LOGGER.debug("Dropping existing tags for user {}", userId);
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("user_id", new SqlParameterValue(Types.OTHER, userId));
    return super.update(USER_TAG_DELETE, paramSourceMap);
  }

  public List<Tag> getAllTags() {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    return super.query(GET_ALL_TAGS, paramSourceMap, new RowMapper<Tag>() {
      @Override
      public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                  .tagId(String.valueOf(rs.getObject("tag_id")))
                  .tagName(rs.getString("tag_name"))
                  .build();
      }
    });
  }

  public List<Tag> getUserTags(String userId) {

    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("user_id", new SqlParameterValue(Types.OTHER, userId));

    return super.query(GET_USER_TAGS, paramSourceMap, new RowMapper<Tag>() {
      @Override
      public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                  .tagId(String.valueOf(rs.getObject("tag_id")))
                  .tagName(rs.getString("tag_name"))
                  .build();
      }
    });
  }


}
