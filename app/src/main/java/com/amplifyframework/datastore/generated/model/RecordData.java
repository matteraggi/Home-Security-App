package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the RecordData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RecordData", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byUser", fields = {"userID"})
public final class RecordData implements Model {
  public static final QueryField ID = field("RecordData", "id");
  public static final QueryField USER = field("RecordData", "userID");
  public static final QueryField TIMESTAMP = field("RecordData", "timestamp");
  public static final QueryField PHOTO = field("RecordData", "photo");
  public static final QueryField CREATED_AT = field("RecordData", "createdAt");
  public static final QueryField UPDATED_AT = field("RecordData", "updatedAt");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "userID", targetNames = {"userID"}, type = User.class) User User;
  private final @ModelField(targetType="String", isRequired = true) String timestamp;
  private final @ModelField(targetType="String") List<String> photo;
  private final @ModelField(targetType="String") String createdAt;
  private final @ModelField(targetType="String") String updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public User getUser() {
      return User;
  }
  
  public String getTimestamp() {
      return timestamp;
  }
  
  public List<String> getPhoto() {
      return photo;
  }
  
  public String getCreatedAt() {
      return createdAt;
  }
  
  public String getUpdatedAt() {
      return updatedAt;
  }
  
  private RecordData(String id, User User, String timestamp, List<String> photo, String createdAt, String updatedAt) {
    this.id = id;
    this.User = User;
    this.timestamp = timestamp;
    this.photo = photo;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RecordData recordData = (RecordData) obj;
      return ObjectsCompat.equals(getId(), recordData.getId()) &&
              ObjectsCompat.equals(getUser(), recordData.getUser()) &&
              ObjectsCompat.equals(getTimestamp(), recordData.getTimestamp()) &&
              ObjectsCompat.equals(getPhoto(), recordData.getPhoto()) &&
              ObjectsCompat.equals(getCreatedAt(), recordData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), recordData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUser())
      .append(getTimestamp())
      .append(getPhoto())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RecordData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("User=" + String.valueOf(getUser()) + ", ")
      .append("timestamp=" + String.valueOf(getTimestamp()) + ", ")
      .append("photo=" + String.valueOf(getPhoto()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TimestampStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static RecordData justId(String id) {
    return new RecordData(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      User,
      timestamp,
      photo,
      createdAt,
      updatedAt);
  }
  public interface TimestampStep {
    BuildStep timestamp(String timestamp);
  }
  

  public interface BuildStep {
    RecordData build();
    BuildStep id(String id);
    BuildStep user(User user);
    BuildStep photo(List<String> photo);
    BuildStep createdAt(String createdAt);
    BuildStep updatedAt(String updatedAt);
  }
  

  public static class Builder implements TimestampStep, BuildStep {
    private String id;
    private String timestamp;
    private User User;
    private List<String> photo;
    private String createdAt;
    private String updatedAt;
    public Builder() {
      
    }
    
    private Builder(String id, User User, String timestamp, List<String> photo, String createdAt, String updatedAt) {
      this.id = id;
      this.User = User;
      this.timestamp = timestamp;
      this.photo = photo;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }
    
    @Override
     public RecordData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RecordData(
          id,
          User,
          timestamp,
          photo,
          createdAt,
          updatedAt);
    }
    
    @Override
     public BuildStep timestamp(String timestamp) {
        Objects.requireNonNull(timestamp);
        this.timestamp = timestamp;
        return this;
    }
    
    @Override
     public BuildStep user(User user) {
        this.User = user;
        return this;
    }
    
    @Override
     public BuildStep photo(List<String> photo) {
        this.photo = photo;
        return this;
    }
    
    @Override
     public BuildStep createdAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
     public BuildStep updatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, User user, String timestamp, List<String> photo, String createdAt, String updatedAt) {
      super(id, User, timestamp, photo, createdAt, updatedAt);
      Objects.requireNonNull(timestamp);
    }
    
    @Override
     public CopyOfBuilder timestamp(String timestamp) {
      return (CopyOfBuilder) super.timestamp(timestamp);
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder photo(List<String> photo) {
      return (CopyOfBuilder) super.photo(photo);
    }
    
    @Override
     public CopyOfBuilder createdAt(String createdAt) {
      return (CopyOfBuilder) super.createdAt(createdAt);
    }
    
    @Override
     public CopyOfBuilder updatedAt(String updatedAt) {
      return (CopyOfBuilder) super.updatedAt(updatedAt);
    }
  }
  

  public static class RecordDataIdentifier extends ModelIdentifier<RecordData> {
    private static final long serialVersionUID = 1L;
    public RecordDataIdentifier(String id) {
      super(id);
    }
  }
  
}
