# April0616-unused
###### \PersonInfoOverview.java
``` java
    /**
     * Set the default contact photo to the default person.
     */
    public void setDefaultContactPhoto() {
        String defaultPhotoPath = "src/main/resources/images/help_icon.png";
        File defaultPhoto = new File(defaultPhotoPath);
        URI defaultPhotoUri = defaultPhoto.toURI();
        Image defaultImage = new Image(defaultPhotoUri.toString());
        centerImage(defaultImage);
        contactPhoto.setImage(defaultImage);
    }

    /**
     * Load the photo of the specified person.
     * @param person
     */
    public void loadPhoto(ReadOnlyPerson person) {
        String photoPath = person.getPhotoPath().value;
        File photo = new File(photoPath);
        URI photoUri = photo.toURI();
        Image image = new Image(photoUri.toString());

        contactPhoto.setPreserveRatio(true);
        centerImage(image);
        contactPhoto.setImage(image);
    }

```
