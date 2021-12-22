import exception.DriverInitFailedError;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.Zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import exception.GADCException;

/**
 * Genshin Auto Daily Check-in
 * @version Java 1.0.0
 * @see <a href="https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in">GitHub Repository</a>
 */

public class GADC {

    private WebDriver driver;
    private final ChromeOptions options;
    public static final String DRIVER_NAME = "chromedriver.exe";
    public static final String AbsPath = new File("welcome.vbs").getAbsolutePath().replace("welcome.vbs", ""); // C:\~path~\

    public static void main(String[] args){
        GADC gadc = new GADC();
        if (!gadc.checkIn()) {
            suspendGADC();
        }
    }

    public GADC() {
        options = new ChromeOptions();

//        Greetings!
        File done = new File("./done.txt");
        if (!done.exists()) {  // If this is the initial execution
            MsgBoxManager.showWelcome();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } /*
        else {
            options.addArguments("headless");
        }

        Will be implemented in next update :) */

        try {
            configChromeVirtualEnv();
            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");  // Set virtual env to remember login token.
        } catch (DriverInitFailedError | IOException e) {
            System.err.println("Failed to compose debug mode. Chrome will work in automation mode and will not save any data");
        }
        System.setProperty("webdriver.chrome.driver", DRIVER_NAME);
        try {
            attachDriver();
        } catch (GADCException | IOException e) {
            suspendGADC();
        }
    }


    /**
     * This part is the main part of GADC.
     * All interactions with Chrome and webpage are executed here.
     * @author ForestHouse2316
     */
    public boolean checkIn() {
        driver.get("https://webstatic-sea.mihoyo.com/ys/event/signin-sea-v3/index.html?act_id=e202102251931481&mhy_auth_required=true");
        driver.findElement(new By.ByXPath("/html/body/div[1]/div[1]/div/div/div/div[2]/div[1]/img")).click();
        try {
            driver.findElement(new By.ByXPath("/html/body/div[4]/div/div/div/img[2]"));
            File done = new File("done.txt");
            if (done.exists()) {
                if (!done.delete()) {
                    // If done.txt is not removed, GADC will start the check-in process, and that'll make it hard dev to analyze the fundamental error
                    System.err.println("Cannot delete done.txt. This must cause an error later");
                    return false;
                }
            }
            MsgBoxManager.showLoginNotice();
            while (true) {  // Observe the existence of done.txt
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                done = new WeakReference<>(new File("done.txt")).get();
                if (done != null && done.exists()) {
                    break;
                } else if (done == null) {  // If done is null due to WeakRef, reassign with the normal object.
                    done = new File("done.txt");
                    if (done.exists()) {
                        done = null;
                        break;
                    }
                    done = null;
                }
            }
        } catch (NoSuchElementException e) {  // If already logged in
            System.out.println("Already logged in");
        }
        try {
            driver.findElement(By.className("components-home-assets-__sign-content_---active---36unD3")).click();
        } catch (NoSuchElementException e) {
            System.out.println("Today's check is already done.");
        }
        return true;
    }

    /**
     * Attach ChromeDriver to driver.
     * Also check chromedriver.exe and take a task when the driver version is old or there is no driver.
     */
    private void attachDriver() throws GADCException, IOException {
        File file = new File("./" + DRIVER_NAME);
        if (!file.exists()){  // If there is no driver
            System.out.println("Driver does not exist.");
            downloadDriverWithRetry();
        } else {
            try {
                driver = new ChromeDriver(options);  // If the driver version does not match with the Chrome version, cause an exception
            } catch (Exception e){  // Caused by a driver version mismatch
                System.out.println("Driver does not compatible to your chrome version.");
                if (!file.delete()) {
                    System.err.println("The driver file has not been deleted completely. It can cause other errors.");
                }
                downloadDriverWithRetry();
                driver = new ChromeDriver(options);  // If this cause an Exception, it may due to OS (such as permission problem, protection...)
            }
        }
    }

    /**
     * Just help try to download driver again if failed to download.
     * @throws DriverInitFailedError Throws when there is no Chrome
     */
    private void downloadDriverWithRetry() throws DriverInitFailedError {
        if (downloadDriver()) {
            System.err.println("Failed to download driver. Trying again. . .");
            if (downloadDriver()) {
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
            System.err.println("Cannot read registry value");
            return false;
        }
        System.out.println("Current Chrome version : " + version);
        version = version.split("\\.")[0];
        try (InputStream latestDriverVersionStream = new URL("https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + version).openStream()) {
            Scanner s = new Scanner(latestDriverVersionStream).useDelimiter("\\A");
            String latestDriverVersion = s.hasNext() ? s.next() : "";
            System.out.println("Latest driver version : " + latestDriverVersion);
            InputStream download = new URL("https://chromedriver.storage.googleapis.com/" + latestDriverVersion + "/chromedriver_win32.zip").openStream();
            Zip.unzip(download, new File("./"));
        } catch (IOException e) {
            System.err.println("Failed to download and unzip chromedriver");
            return false;
        }
        return true;
    }

    /**
     * Set Chrome debug port and virtual environment path.
     * Virtual environment's absolute path can be changed when GADC folder moved to another path.
     * @throws DriverInitFailedError Throws when exception caused except IOException
     * @throws IOException Throws when file handling has not been done completely
     */
    private void configChromeVirtualEnv() throws DriverInitFailedError, IOException{
        File folder = new File(AbsPath+"VirtualEnv");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw new IOException("Cannot make a new directory at " + AbsPath);
            }
        }
        File executor = new File("VirtualEnv.bat");
        try {
            executor.delete();
        } catch (Exception e) {
            System.out.println("An exception caused during deleting VirtualEnv.bat file\n" +
                    "If error caused again from that batch file, check out this exception first.");
        }
        Path batPath = Paths.get(AbsPath + "VirtualEnv.bat");
        try {
            Files.createFile(batPath);
        } catch (IOException e) {
            throw new IOException("Failed to create VirtualEnv.bat");
        }
        String cmd = "\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\" " +
                "--remote-debugging-port=9222 --user-data-dir=" + "\"" + AbsPath + "VirtualEnv" + "\"";  // Location of ChromeVirtualEnv
        byte[] bytes = cmd.getBytes();
        try {
            Files.write(batPath, bytes);  // Create customized batch executor
            System.out.println("VirtualEnv executor has been created successfully");
        } catch (IOException e) {
            throw new IOException("Cannot write into VirtualEnv.bat");
        }
        try {
            Runtime.getRuntime().exec("\"" + AbsPath + "VirtualEnv.bat" + "\"");  // Open debug bridge
        } catch (Exception e) {
            throw new DriverInitFailedError("An unknown error has been caused during executing batch file");
        }
    }

    /**
     * Exit with cleaning driver process and showing failed message.
     */
    public static void suspendGADC() {
        try {
            Runtime.getRuntime().exec("start \"" + AbsPath + "gc.bat" + "\"");
        } catch (IOException e) {
            System.err.println("Failed to kill chromedriver. Please kill process manually");
        }
        MsgBoxManager.showFailed();
        System.exit(0);
    }
}

/**
 * Execute and manage the visual basic script message boxes
 */
class MsgBoxManager {
    public static void showLoginNotice() {
        String path = new File("LoginNotice.vbs").getAbsolutePath();
        try {
            Runtime.getRuntime().exec("wscript \"" + path + "\"");
        } catch (IOException ignored) {}
        System.out.println("VBS : login notice");
    }

    public static void showWelcome() {
        String path = new File("welcome.vbs").getAbsolutePath();
        try {
            Runtime.getRuntime().exec("wscript \""+path+"\"");
        } catch (IOException e) {
            System.out.println("\""+path+"\"");
            e.printStackTrace();
        }
        System.out.println("VBS : welcome");
    }

    public static void showFailed() {
        String path = new File("failed.vbs" ).getAbsolutePath();
        try {
            Runtime.getRuntime().exec("wscript \""+path+"\"");  // Show GADC failed to do check-in automatically...
        } catch (IOException ignored) {}
        System.out.println("VBS : failed");
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        for (StackTraceElement s : stacks) {
            System.out.println(s.toString());
        }
    }
}
