# nbriannl-reused
###### \java\seedu\address\commons\events\ui\ChangeThemeRequestEvent.java
``` java
/**
 * Indicates a request to jump to the list of persons
 */
public class ChangeThemeRequestEvent extends BaseEvent {

    public final String themeToChangeTo;

    public ChangeThemeRequestEvent (String themeToChangeTo) {
        this.themeToChangeTo = themeToChangeTo;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\address\logic\Logic.java
``` java
    void setCurrentTheme(String theme);

```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public void setCurrentTheme(String theme) {
        model.setCurrentTheme(theme);
    }

```
###### \java\seedu\address\logic\parser\DeleteCommandParser.java
``` java
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \java\seedu\address\model\Model.java
``` java
    /** Sets the current theme of the app */
    void setCurrentTheme(String theme);

    /** Returns the current theme in use by the app */
    String getCurrentTheme();

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void setCurrentTheme(String theme) {
        currentTheme = theme;
    }

    @Override
    public String getCurrentTheme() {
        return currentTheme;
    }
```
###### \java\seedu\address\model\UserPrefs.java
``` java
    public String getCurrentTheme() {
        if (currentTheme == null) {
            return "/view/LightTheme.css";
        } else {
            return currentTheme;
        }
    }

    public void setTheme(String theme) {
        this.currentTheme = theme;
    }

    public void updateLastSetTheme(String theme) {
        setTheme(theme);
    }

```
###### \java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Changes the current theme to the given theme.
     */
    public void handleChangeTheme(String theme) {
        if (getRoot().getStylesheets().size() > 1) {
            getRoot().getStylesheets().remove(1);
        }
        getRoot().getStylesheets().add(VIEW_PATH + theme);
    }

```
###### \java\seedu\address\ui\MainWindow.java
``` java
    String getCurrentTheme() {
        return getRoot().getStylesheets().get(1);
    }

```
###### \java\seedu\address\ui\MainWindow.java
``` java
    @Subscribe
    private void handleChangeThemeEvent(ChangeThemeRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleChangeTheme(event.themeToChangeTo);
        logic.setCurrentTheme(getCurrentTheme());
    }
}
```
###### \java\seedu\address\ui\UiManager.java
``` java
    @Override
    public void stop() {
        prefs.updateLastUsedGuiSetting(mainWindow.getCurrentGuiSetting());
        prefs.updateLastSetTheme(mainWindow.getCurrentTheme());
        mainWindow.hide();
        mainWindow.releaseResources();
    }

```
###### \java\seedu\address\ui\UiManager.java
``` java
    /**
     * Sets the given theme as the main theme used in the main window
     * @param theme eg. {@code "DarkTheme.css}
     */
    public void setTheme(String theme) {
        mainWindow.getRoot().getStylesheets().add("/view/" + theme);
    }

```
