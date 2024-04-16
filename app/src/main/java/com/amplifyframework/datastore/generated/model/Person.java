package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;
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

/** This is an auto generated class representing the Person type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "People", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byUser", fields = {"userID"})
public final class Person implements Model {
  public static final QueryField ID = field("Person", "id");
  public static final QueryField USER = field("Person", "userID");
  public static final QueryField INSIDE = field("Person", "inside");
  public static final QueryField PHOTO = field("Person", "photo");
  public static final QueryField NAME = field("Person", "name");
  public static final QueryField FINGERPRINT = field("Person", "fingerprint");
  public static final QueryField NFC = field("Person", "nfc");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "userID", targetNames = {"userID"}, type = User.class) User User;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean inside;
  private final @ModelField(targetType="String") String photo;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String") String fingerprint;
  private final @ModelField(targetType="String") String nfc;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
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
  
  public Boolean getInside() {
      return inside;
  }
  
  public String getPhoto() {
      return photo;
  }
  
  public String getName() {
      return name;
  }
  
  public String getFingerprint() {
      return fingerprint;
  }
  
  public String getNfc() {
      return nfc;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Person(String id, User User, Boolean inside, String photo, String name, String fingerprint, String nfc) {
    this.id = id;
    this.User = User;
    this.inside = inside;
    this.photo = photo;
    this.name = name;
    this.fingerprint = fingerprint;
    this.nfc = nfc;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Person person = (Person) obj;
      return ObjectsCompat.equals(getId(), person.getId()) &&
              ObjectsCompat.equals(getUser(), person.getUser()) &&
              ObjectsCompat.equals(getInside(), person.getInside()) &&
              ObjectsCompat.equals(getPhoto(), person.getPhoto()) &&
              ObjectsCompat.equals(getName(), person.getName()) &&
              ObjectsCompat.equals(getFingerprint(), person.getFingerprint()) &&
              ObjectsCompat.equals(getNfc(), person.getNfc()) &&
              ObjectsCompat.equals(getCreatedAt(), person.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), person.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUser())
      .append(getInside())
      .append(getPhoto())
      .append(getName())
      .append(getFingerprint())
      .append(getNfc())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Person {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("User=" + String.valueOf(getUser()) + ", ")
      .append("inside=" + String.valueOf(getInside()) + ", ")
      .append("photo=" + String.valueOf(getPhoto()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("fingerprint=" + String.valueOf(getFingerprint()) + ", ")
      .append("nfc=" + String.valueOf(getNfc()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static InsideStep builder() {
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
  public static Person justId(String id) {
    return new Person(
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
      User,
      inside,
      photo,
      name,
      fingerprint,
      nfc);
  }
  public interface InsideStep {
    NameStep inside(Boolean inside);
  }
  

  public interface NameStep {
    BuildStep name(String name);
  }
  

  public interface BuildStep {
    Person build();
    BuildStep id(String id);
    BuildStep user(User user);
    BuildStep photo(String photo);
    BuildStep fingerprint(String fingerprint);
    BuildStep nfc(String nfc);
  }
  

  public static class Builder implements InsideStep, NameStep, BuildStep {
    private String id;
    private Boolean inside;
    private String name;
    private User User;
    private String photo;
    private String fingerprint;
    private String nfc;
    public Builder() {
      
    }
    
    private Builder(String id, User User, Boolean inside, String photo, String name, String fingerprint, String nfc) {
      this.id = id;
      this.User = User;
      this.inside = inside;
      this.photo = photo;
      this.name = name;
      this.fingerprint = fingerprint;
      this.nfc = nfc;
    }
    
    @Override
     public Person build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Person(
          id,
          User,
          inside,
          photo,
          name,
          fingerprint,
          nfc);
    }
    
    @Override
     public NameStep inside(Boolean inside) {
        Objects.requireNonNull(inside);
        this.inside = inside;
        return this;
    }
    
    @Override
     public BuildStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep user(User user) {
        this.User = user;
        return this;
    }
    
    @Override
     public BuildStep photo(String photo) {
        this.photo = photo;
        return this;
    }
    
    @Override
     public BuildStep fingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }
    
    @Override
     public BuildStep nfc(String nfc) {
        this.nfc = nfc;
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
    private CopyOfBuilder(String id, User user, Boolean inside, String photo, String name, String fingerprint, String nfc) {
      super(id, User, inside, photo, name, fingerprint, nfc);
      Objects.requireNonNull(inside);
      Objects.requireNonNull(name);
    }
    
    @Override
     public CopyOfBuilder inside(Boolean inside) {
      return (CopyOfBuilder) super.inside(inside);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder photo(String photo) {
      return (CopyOfBuilder) super.photo(photo);
    }
    
    @Override
     public CopyOfBuilder fingerprint(String fingerprint) {
      return (CopyOfBuilder) super.fingerprint(fingerprint);
    }
    
    @Override
     public CopyOfBuilder nfc(String nfc) {
      return (CopyOfBuilder) super.nfc(nfc);
    }
  }
  

  public static class PersonIdentifier extends ModelIdentifier<Person> {
    private static final long serialVersionUID = 1L;
    public PersonIdentifier(String id) {
      super(id);
    }
  }
  
}
