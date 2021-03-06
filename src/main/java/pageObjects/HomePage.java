package pageObjects;

import classesUtilities.Page;
import classesUtilities.StepsLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;


public class HomePage extends Page {
    private By searchField = By.cssSelector("a.noo-search");
    private By searchPopUp = By.cssSelector("input.form-control");
    private By cartIcon = By.cssSelector("span.cart-name-and-total");
    private By disclaimer = By.cssSelector("p[data-notice-id = '1013467fc0b780504faafa9d866c07ac']");
    private By dismissDisclaimer = By.linkText("Dismiss");
    private By myAccountLink = By.linkText("My Account");
    private By productThumbnails = By.cssSelector("div.noo-product-inner");


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String getCartText() {
        getPageTitle();
        return getWebElementText(cartIcon, "CART");
    }

    public String getSearchFieldText() {
        return getWebElementText(searchField, "SEARCH FIELD");
    }

    public SearchPage searchForAnItem(String text) {
        clickOnElement(searchField, "SEARCH FIELD");
        type(searchPopUp, "SEARCH FORM", text + Keys.ENTER);
        return new SearchPage(driver);
    }


    public String findLocatorOfPageSection(String key){
        /*
         * for entered name of the home page section, switch expression returns the unique locator
         */
        return switch(key){
            case "umbra blue" -> "#slide-6-layer-4";
            case "free shipping" -> "div.vc_custom_1465282622143";
            case "ladies" -> "div.vc_custom_1465285769156";
            case "men" -> "div.vc_custom_1465550716269";
            case "reviews" -> "div.vc_custom_1554631440321";
            case "fashion news" -> "div.noo-shblog-header";
            case "sign up" -> "div.vc_custom_1554631514516";
            case "footer" -> "footer.wrap-footer";
            default -> "Provide the correct home page section name!";
        };
    }

    public String scrollUntilHomePageSection(String key) {
        String elementName = key.toUpperCase() + " PAGE SECTION";
        WebElement section = getWebElement(By.cssSelector(findLocatorOfPageSection(key)), elementName);
        scrollUntilElement(section);
        return getWebElementText(By.cssSelector(findLocatorOfPageSection(key)), "TITLE - "+ elementName);
    }

    public void removeDisclaimerFromHeader(){
        WebElement disclaimerRibbon = getWebElement(disclaimer, "DISCLAIMER");
        if(disclaimerRibbon.isDisplayed()){
            disclaimerRibbon.findElement(dismissDisclaimer).click();
           new StepsLogger().info("Disclaimer ribbon is safely removed.");
        } else {
            new StepsLogger().error("Disclaimer ribbon is still covering header.");
        }
    }

    public MyAccountPage goToMyAccountPage() {
        removeDisclaimerFromHeader();
        waitForElementClickability(myAccountLink);
        clickOnElement(myAccountLink, "LINK TO MY ACCOUNT PAGE");
        return new MyAccountPage(driver);
    }

//    private Map<String, WebElement> createPageSectionsMap() {
//        List<String> sectionNames = List.of("top_banner", "four_icons", "products_girl",
//                "products_boy", "reviews",
//                "fashion_news", "sign_up_banner");
//        List<WebElement> sections = driver.findElements(homePageSections);
//        Map<String, WebElement> sectionsMap = new HashMap<>();
//        for (String sectionName : sectionNames) {
//            WebElement section = sections.listIterator().next();
//            sectionsMap.put(sectionName, section);
//        }
//        new StepsLogger().info("Home page sections are: " + sectionsMap.keySet().toString().toUpperCase());
//        return sectionsMap;
//    }

    private WebElement getHomePageSection(By locator){
        return getWebElement(locator, "HP SECTION");
    }

    public WebElement getProductSection(){
        String locator = findLocatorOfPageSection("ladies");
        scrollUntilHomePageSection("ladies");
        return getHomePageSection(By.cssSelector(locator));
    }

    private List<WebElement> getAllProductsFromHomePage(){
        return driver.findElements(productThumbnails);
    }

    public ProductPage clickOnARandomProduct(){
        Random random = new Random();
        WebElement product =  getAllProductsFromHomePage().get(random.nextInt(6));
        scrollUntilHomePageSection("ladies");
        clickOnElement(product, product.getText());
        return new ProductPage(driver);
    }



}
