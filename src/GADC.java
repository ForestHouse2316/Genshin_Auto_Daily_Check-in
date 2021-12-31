import exception.DriverInitFailedError;
import exception.GADCException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Genshin Auto Daily Check-in
 * @version 1.0.2-beta.3  // TODO Change version before release
 * @see <a href="https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in">GitHub Repository</a>
 */
public class GADC {
    private WebDriver driver;
    private final WebDriverWait EWait;
    private Actions actions;
    private final ChromeOptions options;
    public static final String DRIVER_PATH = "scripts\\chromedriver.exe";

    public static void main(String[] args){
        GADC gadc = null;
        try {
            gadc = new GADC();
            if (!gadc.checkIn()) {
                gadc.suspendGADC();
            }
        } catch (Exception e) {  // If unhandled Exception causes
            SaveDataManager.writeStackLog(e);
            if (gadc != null) {
                gadc.suspendGADC();
            }
        }
    }

    public GADC() {
        options = new ChromeOptions();

//        Greetings!
        if (!new File(SaveDataManager.DATA_PATH.toString()).exists()) {  // If this is the initial execution
            MsgBoxManager.showWelcome();
            try {
                SaveDataManager.createDataFile();
            } catch (IOException e) {
                SaveDataManager.writeStackLog(e);
                System.err.println("Cannot create data file");
                suspendGADC();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } /*
        else {
            options.addArguments("headless");
        }

        Will be implemented in next update :) */
        try {
            if (SaveDataManager.readDate().equals(SaveDataManager.DAY_INFO)) {
                System.out.println("Already checked-in");
                System.exit(0);
            }
        } catch (IndexOutOfBoundsException ignored) {}  // If data.txt is null
        try {
            configChromeVirtualEnv();
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");  // Set virtual env to remember login token.
        } catch (DriverInitFailedError | IOException e) {
            System.err.println("Failed to compose debug mode. Chrome will work in automation mode and will not save any data");
        }
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        try {
            attachDriver();
        } catch (GADCException | IOException e) {
            suspendGADC();
        }
        EWait =  new WebDriverWait(driver, Duration.ofMillis(5000));
    }


    /**
     * This part is the main part of GADC.
     * All interactions with Chrome and webpage are executed here.
     * @author ForestHouse2316
     * TODO Refactor code : Modularize Ewait.until()
     */
    public boolean checkIn() {
        driver.get("https://www.hoyolab.com/");
        try{  // If there is no cookie, select Genshin as a main topic automatically
            EWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__layout\"]/div/div[4]/div/div/div/div[2]/div[1]"))).click();  // Genshin button
            driver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div[4]/div/div/div/div[3]/div")).click();  // Confirm button
            Thread.sleep(5000);
        } catch (NoSuchElementException | TimeoutException | InterruptedException e) {
            System.out.println("Topic selecting is not required");
        }

        try {  // Check login status
            EWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__layout\"]/div/div[1]/div/div/div[3]/div[3]/div[1]"))).click();  // Profile img
            EWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"__layout\"]/div/div[1]/div/div/div[3]/div[3]/div[2]/div[1]/div[3]/ul/li/div")));  // Check is there a log-out button, raise exception when need login
            System.out.println("Already logged in");
        } catch (NoSuchElementException | TimeoutException e) {  // If log-out state
            WebElement loginBtn = driver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div[1]/div/div/div[3]/div[3]/div[2]/div[1]/div[2]/ul/li/div"));  // Log-in button
            clickAfterHovering(loginBtn);

            // Login observing
            MsgBoxManager.showLoginNotice();
            while (true) {
                try {
                    driver.findElement(By.xpath("/html/body/div[6]/div/div/div/img[2]"));  // Log in popup's close button, wait until login window close
                } catch (NoSuchElementException e2) {
                    try {
                        Thread.sleep(3000);  // Wait for three seconds to give the term that site change its HTML
                    } catch (InterruptedException ignored) {}
                    break;
                }
                try {
                    Thread.sleep(100);  // Observing interval
                } catch (InterruptedException ignored) {}
            }
        }

        WebElement toolBox = driver.findElement(By.xpath("//*[@id=\"__layout\"]/div/div[2]/div/div/div[1]/div/div/div/div/div/div[1]/div[6]/span"));  // Tool box icon
        clickAfterHovering(toolBox);
        WebElement checkInMenu = EWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"__layout\"]/div/div[2]/div/div/div[2]/div[1]/div[2]/div[1]")));  // Check-in menu
        clickAfterHovering(checkInMenu);  // Now move to check-in site

        driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);  // Focus HoYoLAB
        driver.close();  // Close HoYoLAB tab, because check-in site is opened in a new tab
        driver.switchTo().window((String) driver.getWindowHandles().toArray()[0]);  // Focus Check-in site
        try {
            Thread.sleep(2000);
            driver.findElement(By.className("components-home-assets-__sign-content_---active---36unD3")).click();  // Today's location
            Thread.sleep(2000);
            System.out.println("Checked");
        } catch (NoSuchElementException | TimeoutException | InterruptedException e) {
            System.out.println("Today's check-in is already done");
        }
        SaveDataManager.writeDate();
        cleanDriverProcess();
        MsgBoxManager.showComplete();
        System.out.println("Check-in task has finished");
        return true;
    }

    /**
     * Attach ChromeDriver to driver.
     * Also check chromedriver.exe and take a task when the driver version is old or there is no driver.
     */
    private void attachDriver() throws GADCException, IOException {
        File file = new File("./" + DRIVER_PATH);
        if (!file.exists()){  // If there is no driver
            System.out.println("Driver does not exist.");
            downloadDriverWithRetry();
        } else {
            try {
                driver = new ChromeDriver(options);  // If the driver version does not match with the Chrome version, cause an exception
                return;
            } catch (Exception e){  // Caused by a driver version mismatch
                System.out.println("Driver does not compatible to your chrome version.");
                if (!file.delete()) {
                    System.err.println("The driver file has not been deleted completely. It can cause other errors.");
                }
                downloadDriverWithRetry();
            }
        }
        driver = new ChromeDriver(options);  // If this cause an Exception, it may due to OS (such as permission problem, protection...)
    }

    /**
     * Just help try to download driver again if failed to download.
     * @throws DriverInitFailedError Throws when there is no Chrome
     */
    private void downloadDriverWithRetry() throws DriverInitFailedError {
        if (!downloadDriver()) {
            System.err.println("Failed to download driver. Trying again. . .");
            if (!downloadDriver()) {
                throw new DriverInitFailedError("Cannot download driver");
            }
        }
    }

    /**
     * Download the driver file compatible to current Chrome version.
     * @return true when succeeded to download appropriate driver, false when failed
     * @throws DriverInitFailedError Throws when there is no Chrome
     */
    private boolean downloadDriver() throws DriverInitFailedError {
        BufferedInputStream input;
        String value;
        String version;
        try {
            String command = "reg query " + "\"HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon\" /v version";
            Process process = Runtime.getRuntime().exec(command);
            input = new BufferedInputStream(process.getInputStream());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            value = new String(buffer);
            input.close();
            int index = value.indexOf("REG_SZ");
            if (index<0) {
                throw new DriverInitFailedError("Cannot continue task because there is no Chrome in this device");
            }
            version = value.substring(index + "REG_SZ".length()).trim();
        } catch (Exception e) {
            System.err.println("Cannot readOneLine registry value");
            return false;
        }
        System.out.println("Current Chrome version : " + version);
        version = version.split("\\.")[0];
        try (InputStream latestDriverVersionStream = new URL("https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + version).openStream()) {
            Scanner s = new Scanner(latestDriverVersionStream).useDelimiter("\\A");
            String latestDriverVersion = s.hasNext() ? s.next() : "";
            System.out.println("Latest driver version : " + latestDriverVersion);
            InputStream download = new URL("https://chromedriver.storage.googleapis.com/" + latestDriverVersion + "/chromedriver_win32.zip").openStream();
            Zip.unzip(download, new File("./scripts/"));
            return true;
        } catch (IOException e) {
            System.err.println("Failed to download and unzip chromedriver");
            return false;
        }
    }

    /**
     * Set Chrome debug port and virtual environment path.
     * Virtual environment's absolute path can be changed when GADC folder moved to another path.
     * @throws DriverInitFailedError Throws when exception caused except IOException
     * @throws IOException Throws when file handling has not been done completely
     */
    private void configChromeVirtualEnv() throws DriverInitFailedError, IOException{
        final String BATCH_PATH = SaveDataManager.AbsPath+"scripts\\VirtualEnv.bat";
        final String ENV_PATH = SaveDataManager.AbsPath+"VirtualEnv";
        File folder = new File(ENV_PATH);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw new IOException("Cannot make a new directory at " + SaveDataManager.AbsPath);
            }
        }
        File executor = new File(BATCH_PATH);
        try {
            executor.delete();
        } catch (Exception e) {
            System.out.println("An exception caused during deleting VirtualEnv.bat file\n" +
                    "If error caused again from that batch file, check out this exception first.");
        }
        Path batPath = Paths.get(BATCH_PATH);
        try {
            Files.createFile(batPath);
        } catch (IOException e) {
            throw new IOException("Failed to create VirtualEnv.bat");
        }
        String cmd = "\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\" " +
                "--remote-debugging-port=9222 --user-data-dir=" + "\"" + ENV_PATH + "\"";  // Location of ChromeVirtualEnv
        byte[] bytes = cmd.getBytes();
        try {
            Files.write(batPath, bytes);  // Create customized batch executor
            System.out.println("VirtualEnv executor has been created successfully");
        } catch (IOException e) {
            throw new IOException("Cannot write into VirtualEnv.bat");
        }
        try {
            Runtime.getRuntime().exec("\"" + BATCH_PATH + "\"");  // Open debug bridge
        } catch (Exception e) {
            throw new DriverInitFailedError("An unknown error has been caused during executing batch file");
        }
    }

    /**
     * Use hover option to disguise as a human
     */
    private void clickAfterHovering(WebElement element) {
        if (actions == null) { actions = new Actions(driver); }
        actions.moveToElement(element).build().perform();  // Hover
        try {
            Thread.sleep(200 + ((int) (Math.random() * 300)));  // Wait 200~500 millis
        } catch (InterruptedException ignored) {}
        element.click();  // Click
    }

    /**
     * Exit with cleaning driver process and showing failed message.
     */
    public void suspendGADC() {
        cleanDriverProcess();
        MsgBoxManager.showFailed();
        System.exit(0);
    }

    /**
     * Clean driver process
     */
    private void cleanDriverProcess() {
        try {
            if (driver != null) {
                driver.close();
                driver.quit();
                driver = null;
            }
            Thread.sleep(3000);  // Wait for 3sec before execute gc.bat
            Runtime.getRuntime().exec("taskkill /im scripts\\chromedriver.exe /f /t");
        } catch (Exception e) {  // Catch the IOException, InterruptedException, and unknown Exception
            System.err.println("Failed to kill chromedriver. Please kill process manually");
        }
    }
}

/**
 * Execute and manage the visual basic script message boxes
 */
class MsgBoxManager {
    private static final String LOGIN_NOTICE_VBS_PATH = SaveDataManager.AbsPath + "scripts\\LoginNotice.vbs";
    private static final String WELCOME_VBS_PATH =SaveDataManager.AbsPath + "scripts\\welcome.vbs";
    private static final String FAILED_VBS_PATH = SaveDataManager.AbsPath + "scripts\\failed.vbs";
    private static final String COMPLETE_VBS_PATH = SaveDataManager.AbsPath + "scripts\\complete.vbs";

    public static void showLoginNotice() {
        try {
            Runtime.getRuntime().exec("wscript \"" + LOGIN_NOTICE_VBS_PATH + "\"");
        } catch (IOException ignored) {}
        System.out.println("VBS : login notice");
    }

    public static void showWelcome() {
        try {
            Runtime.getRuntime().exec("wscript \"" + WELCOME_VBS_PATH + "\"");
        } catch (IOException ignored) {}
        System.out.println("VBS : welcome");
    }

    public static void showFailed() {
        try {
            Runtime.getRuntime().exec("wscript \"" + FAILED_VBS_PATH + "\"");  // Show GADC failed to do check-in automatically...
        } catch (IOException ignored) {}
        System.out.println("VBS : failed");
    }

    public static void showComplete() {
        try {
            Runtime.getRuntime().exec("wscript \"" + COMPLETE_VBS_PATH + "\"");  // Show GADC failed to do check-in automatically...
        } catch (IOException ignored) {}
        System.out.println("VBS : complete");
    }
}

/**
 * Manage data.txt file.
 */
class SaveDataManager {
    public static final String DAY_INFO;
    public static final String TIME_INFO;
    public static final String CURRENT_HOUR;
    public static final String AbsPath = new File("scripts\\welcome.vbs").getAbsolutePath().replace("scripts\\welcome.vbs", "");  // C:\~path~\
    public static final Path DATA_PATH = Paths.get(AbsPath + "\\scripts\\data.txt");
    public static final String GADC_VERSION = "1.0.2-beta.3";  // TODO Change version before release

    static {
        String[] date = new Date().toString().split(" ");
        TIME_INFO = date[3].replace(":", "_");
        CURRENT_HOUR = TIME_INFO.split("_")[0];
        DAY_INFO = Integer.parseInt(CURRENT_HOUR) >= 1 ? date[2] : String.valueOf((Integer.parseInt(date[2]) - 1));  // HoYoLAB server is initialized at 1 a.m.
        System.out.println("Current hour : " + CURRENT_HOUR);
        System.out.println("Server date : " + DAY_INFO);
    }

    public static void createDataFile() throws IOException{
        Files.write(DATA_PATH, "init".getBytes());
    }
    public static String readDate() {
        try {
            List<String> lines = Files.readAllLines(DATA_PATH);
            return lines.get(0);
        } catch (IOException e) {
            return "";
        }
    }
    public static void writeDate() {
        try {
            new File(DATA_PATH.toString()).delete();
        } catch (Exception ignored) {}
        try {
            Files.write(DATA_PATH, DAY_INFO.getBytes());
        } catch (IOException ignored) {}
    }
    public static void writeStackLog(Exception e) {
        String name = e.getClass().getCanonicalName();
        String msg = e.getMessage();
        StackTraceElement[] stacks = e.getStackTrace();
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("GADC Version : ").append(GADC_VERSION).append("\n\n").append("Exception name : ").append(name!=null ? name : "").append("     ").append(msg).append("\n");
        for (StackTraceElement s : stacks) {
            logBuilder.append(s).append("\n");
        }
        byte[] bytes = logBuilder.toString().getBytes();
        try {
            Files.write(Paths.get(AbsPath + TIME_INFO + "_crash.log"), bytes);
        } catch (IOException ignored) {}
    }
}