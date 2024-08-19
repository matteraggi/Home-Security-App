package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
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

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class User implements Model {
  public static final QueryField ID = field("User", "id");
  public static final QueryField ALARM = field("User", "alarm");
  public static final QueryField DEVICE_IDS = field("User", "deviceIds");
  public static final QueryField UPDATED_AT = field("User", "updatedAt");
  public static final QueryField CREATED_AT = field("User", "createdAt");
  public static final QueryField THINGS_IDS = field("User", "thingsIds");
  public static final QueryField PIN = field("User", "pin");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Person") @HasMany(associatedWith = "User", type = Person.class) List<Person> People = null;
  private final @ModelField(targetType="RecordData") @HasMany(associatedWith = "User", type = RecordData.class) List<RecordData> RecordData = null;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean alarm;
  private final @ModelField(targetType="String") List<String> deviceIds;
  private final @ModelField(targetType="String") String updatedAt;
  private final @ModelField(targetType="String") String createdAt;
  private final @ModelField(targetType="String") List<String> thingsIds;
  private final @ModelField(targetType="String") String pin;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public List<Person> getPeople() {
      return People;
  }
  
  public List<RecordData> getRecordData() {
      return RecordData;
  }
  
  public Boolean getAlarm() {
      return alarm;
  }
  
  public List<String> getDeviceIds() {
      return deviceIds;
  }
  
  public String getUpdatedAt() {
      return updatedAt;
  }
  
  public String getCreatedAt() {
      return createdAt;
  }
  
  public List<String> getThingsIds() {
      return thingsIds;
  }
  
  public String getPin() {
      return pin;
  }
  
  private User(String id, Boolean alarm, List<String> deviceIds, String updatedAt, String createdAt, List<String> thingsIds, String pin) {
    this.id = id;
    this.alarm = alarm;
    this.deviceIds = deviceIds;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
    this.thingsIds = thingsIds;
    this.pin = pin;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getAlarm(), user.getAlarm()) &&
              ObjectsCompat.equals(getDeviceIds(), user.getDeviceIds()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getThingsIds(), user.getThingsIds()) &&
              ObjectsCompat.equals(getPin(), user.getPin());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getAlarm())
      .append(getDeviceIds())
      .append(getUpdatedAt())
      .append(getCreatedAt())
      .append(getThingsIds())
      .append(getPin())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("alarm=" + String.valueOf(getAlarm()) + ", ")
      .append("deviceIds=" + String.valueOf(getDeviceIds()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("thingsIds=" + String.valueOf(getThingsIds()) + ", ")
      .append("pin=" + String.valueOf(getPin()))
      .append("}")
      .toString();
  }
  
  public static AlarmStep builder() {
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
  public static User justId(String id) {
    return new User(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      alarm,
      deviceIds,
      updatedAt,
      createdAt,
      thingsIds,
      pin);
  }
  public interface AlarmStep {
    BuildStep alarm(Boolean alarm);
  }
  

  public interface BuildStep {
    User build();
    BuildStep id(String id);
    BuildStep deviceIds(List<String> deviceIds);
    BuildStep updatedAt(String updatedAt);
    BuildStep createdAt(String createdAt);
    BuildStep thingsIds(List<String> thingsIds);
    BuildStep pin(String pin);
  }
  

  public static class Builder implements AlarmStep, BuildStep {
    private String id;
    private Boolean alarm;
    private List<String> deviceIds;
    private String updatedAt;
    private String createdAt;
    private List<String> thingsIds;
    private String pin;
    public Builder() {
      
    }
    
    private Builder(String id, Boolean alarm, List<String> deviceIds, String updatedAt, String createdAt, List<String> thingsIds, String pin) {
      this.id = id;
      this.alarm = alarm;
      this.deviceIds = deviceIds;
      this.updatedAt = updatedAt;
      this.createdAt = createdAt;
      this.thingsIds = thingsIds;
      this.pin = pin;
    }
    
    @Override
     public User build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new User(
          id,
          alarm,
          deviceIds,
          updatedAt,
          createdAt,
          thingsIds,
          pin);
    }
    
    @Override
     public BuildStep alarm(Boolean alarm) {
        Objects.requireNonNull(alarm);
        this.alarm = alarm;
        return this;
    }
    
    @Override
     public BuildStep deviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
        return this;
    }
    
    @Override
     public BuildStep updatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    
    @Override
     public BuildStep createdAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    @Override
     public BuildStep thingsIds(List<String> thingsIds) {
        this.thingsIds = thingsIds;
        return this;
    }
    
    @Override
     public BuildStep pin(String pin) {
        this.pin = pin;
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
    private CopyOfBuilder(String id, Boolean alarm, List<String> deviceIds, String updatedAt, String createdAt, List<String> thingsIds, String pin) {
      super(id, alarm, deviceIds, updatedAt, createdAt, thingsIds, pin);
      Objects.requireNonNull(alarm);
    }
    
    @Override
     public CopyOfBuilder alarm(Boolean alarm) {
      return (CopyOfBuilder) super.alarm(alarm);
    }
    
    @Override
     public CopyOfBuilder deviceIds(List<String> deviceIds) {
      return (CopyOfBuilder) super.deviceIds(deviceIds);
    }
    
    @Override
     public CopyOfBuilder updatedAt(String updatedAt) {
      return (CopyOfBuilder) super.updatedAt(updatedAt);
    }
    
    @Override
     public CopyOfBuilder createdAt(String createdAt) {
      return (CopyOfBuilder) super.createdAt(createdAt);
    }
    
    @Override
     public CopyOfBuilder thingsIds(List<String> thingsIds) {
      return (CopyOfBuilder) super.thingsIds(thingsIds);
    }
    
    @Override
     public CopyOfBuilder pin(String pin) {
      return (CopyOfBuilder) super.pin(pin);
    }
  }
  

  public static class UserIdentifier extends ModelIdentifier<User> {
    private static final long serialVersionUID = 1L;
    public UserIdentifier(String id) {
      super(id);
    }
  }
  
}
