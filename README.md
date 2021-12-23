[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# GADC 가득
<img src="/Documents/GADC.png?raw=true" width = "300px" height = "300px" align = "right">

> Auto Daily Check-in for Genshin Impact

Are you suffering from forgetting to do HoYoLAB check-in? Use this and be free from it!\
원신 일일 출첵 매일 까먹으셔서 당황하셨어요? 저도 많이 당황했습니다 ^^\
자동 출첵 한번 잡숴보세요 ;)

[최신 버전 다운로드](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/releases/tag/GADC-1.0.1)\
[DOWNLOAD Latest Version](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/releases/tag/GADC-1.0.1)

-----

## Notice!!! - How to update GADC manually
- If you want to do update manually, please keep your VirtualEnv folder to maintain your settings.\
Move VirtualEnv folder into the folder of a new version of GADC. Place VirtualEnv folder to same location of GADC.jar and scripts folder.
- 만약 수동으로 업데이트를 하고자 하신다면, 셋팅값 유지를 위해 VirtualEnv 폴더는 유지해주시기 바랍니다.\
새 버전의 GADC 폴더로 VirtualEnv 폴더를 옮겨주세요. GADC.jar 와 scripts 폴더와 같은 위치에 놓아주세요.


-----

## ❔ How to use

### 0. Download it
Download latest version of GADC. It's in a zip file. (EN - English Version)\
The file you need to execute is "GADC.jar"\
최신 버전의 GADC을 다운받아주세요. 압축 파일로 되어 있습니다. (KR - 한국어 버전)\
여러분이 실행해야할 파일은 "GADC.jar" 입니다.

### 1. Install Java
You just need JRE(or JDK). It's the same method as installing Java for Minecraft(Java Edition)!
If it's already installed, you don't have to re-install it.\
__JRE/JDK 1.8 or higher version needed__

JRE나 JDK만 있으시면 됩니다. 마인크래프트 자바에디션 깔 때 자바 까는 것과 똑같이 깔아주시면 됩니다!\
이미 깔려있으면 다시 깔으실 필요 없어요\
__JRE/JDK 1.8 또는 그 이상의 버전이 필요합니다.__

### 2. Register into starting program (Optional)
Download the file, and register it into starting program. Then it'll automatically do check-in once every day ^^\
If it's hard to register GADC into startup program, it's okay not to do that. Just execute GADC everyday.\
We'll prepare that function ASAP.
> Notice : The Chrome remoted by GADC will be executed in independent environment.


파일을 다운받고, 시작프로그램에 등록만 하면 알아서~ 매일 한 번, 컴퓨터를 킬 때마다 자동으로 출첵을 해준답니다 ^^\
만약 jre를 시작프로그램으로 등록하는 과정이 어렵다면, 하지 않으셔도 괜찮습니다. 매일 한 번 실행시켜 주시면 됩니다.\
최대한 빠른 시일 내로 해당 기능을 준비하도록 하겠습니다.
> 공지 : GADC에 의해 제어되는 크롬은 여러분이 쓰시는 크롬과 다른, 독립된 환경에서 실행됩니다.


### 3. Do your login just "ONCE"
This program supports only Chrome browser(not Chromium!) and you need to login to HoYoLAB in Chrome
because this progrma __does not__ save your HoYoLAB ID/PW.\
HoYoLAB site basically support 'remember me' function. So, you don't have to do extra settings.\
But, if auto-login goes disabled, then you need to login again.\
이 프로그램은 크롬 브라우저만 지원하며 크로미움은 지원하지 않아요 :(\
호요랩이 기본적으로 로그인 유지 기능을 지원하므로 추가적인 설정을 하실 필요는 없습니다.\
단, 비밀번호 변경 등 자동 로그인 상태가 풀리는 경우에는 다시 로그인을 해야할 수 있습니다.

That's all!\
설정은 이게 끝이에요!


## ❔ How it works, and what is that files
- This program use Chrome's debug mode. It can make indepndent Chrome environment and GADC connect to HoYoLAB using that.
So GADC cannot access anything in your "REAL" Chrome(a.k.a. your main Chrome). Of course, also does not access to debug Chrome's data.
GADC.jar is the main file of GADC. It controls all process and flows when you execute GADC. VBScripts just show you message boxes.
And finally, gc.bat clean the memory by suspend chrome driver process and VirtualEnv.bat compose debug bridge between GADC and debug Chrome.
data.txt just represents the lastest day of your check-in.
- 이 프로그램은 크롬의 디버그 모드를 사용합니다. 디버그 모드는 독립된 크롬 환경을 만들 수 있고 GADC은 이를 이용하여 호요랩에 접속합니다.
그러므로 GADC은 여러분의 실제 크롬(즉 그냥 쓰시는 메인 크롬)의 어떠한 것에도 접근할 수 없습니다. 당연하지만, 디버그 크롬의 데이터에도 접근하지 않습니다.
GADC.jar은 GADC의 메인 파일입니다. 모든 프로세스와 흐름을 제어하죠. VB스크립트는 그저 여러분에게 알림창을 띄웁니다.
그리고 마지막으로, gc.bat은 크롬 드라이버 프로세스를 종료하여 메모리를 정리하며 VirtualEnv.bat은 GADC과 디버그 크롬 사이에 디버그 브릿지를 구성합니다.
data.txt는 마지막으로 출석체크를 한 날짜를 나타냅니다.

-----

## ⚖️ Terms&Policy

### About source code disclosure
- All resources of GADC are following open-source policy. You can check all codes of GADC in [scripts](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/scripts_en) and [src](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/src).
- GADC의 모든 리소스는 오픈소스 정책을 따릅니다. [스크립트 폴더](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/scripts)와 [소스코드 폴더](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/src)에서 GADC의 모든 코드들을 확인하실 수 있습니다.

### About source code disclosure
- All resources of GADC are following open-source policy. You can check all codes of GADC in [scripts](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/scripts_en) and [src](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/src).
- GADC의 모든 리소스는 오픈소스 정책을 따릅니다. [스크립트 폴더](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/scripts)와 [소스코드 폴더](https://github.com/ForestHouse2316/Genshin_Auto_Daily_Check-in/tree/main/src)에서 GADC의 모든 코드들을 확인하실 수 있습니다.

### The range of GADC
- This Terms&Policy is only applied to GADC and GADC's resources except 3rd-party library, Chrome, and Sites that GADC connet to.
Please follow each Terms&Policy when you use those services.
- 이 약관 및 정책은 오직 GADC과 GADC의 리소스에만 적용됩니다. 서드파티 라이브러리, 크롬, GADC이 접속하는 사이트 등은 제외하며 이용 시 각각의 이용약관을 준수해주시기 바랍니다.

### Collecting information
- GADC does not collect or use any kind of user data.
If GADC collect your data or behave suspiciously, immediately check that the GADC you're using is downloaded in official GitHub repository.
GitHub is the only site that distributes GADC.\
Developer(include contributors) does not have any responsibility for the issue caused by duplicated GADC being distributed in other sites.
- GADC은 어떠한 형태의 유저 정보도 수집 및 이용하지 않습니다.
만약 GADC이 그러한 행동을 보인다면, 바로 공식 GitHub 사이트에서 받은 GADC이 맞는지 확인하시기 바랍니다.
GADC은 오직 GitHub에서만 배포됩니다.\
타 사이트에서 배포된 GADC의 사본에서 일어나는 문제에 대해서는 어떠한 책임도 지지 않습니다.

### Data Access
- GADC access your registry value to get user's chrome version so that download the appropriate chromedriver.
Except this, GADC does not access other part of user's storage and OS.
All other tasks work in the GADC folder.
- GADC은 알맞은 크롬 드라이버를 다운받고자 레지스트리로부터 사용자의 크롬 버전을 얻어냅니다.
이것을 제외하고, GADC은 사용자의 저장소나 OS에 접근하지 않습니다.
다른 모든 작업들은 GADC 폴더 안에서 이루어집니다.
```
Accessing registry path is : HKEY_CURRENT_USER\Software\Google\Chrome\BLBeacon\version
```

### Internet Access
- GADC only uses the internet when GADC needs to download(or update) chrome driver or latest version of GADC. There is no internet connection except that.
- GADC은 크롬 드라이버를 다운로드 하거나 업데이트 할 때에만 인터넷을 사용합니다. 이외의 인터넷 연결은 전혀 없습니다.

### Responsibility
- Developers are responsible for their code writing, but not responsible for other issues.\
This means that developers will try to serve high-quality code to users, but if there are problems such as missing check-in, developers do not have any responsibility for that.\
Especially, as written above, when critical problems cause by non-official GADC distribution, it is totally the user's fault.\
Make sure that the GADC is not a perfect program. We'll try to make it better, but it doesn't mean there are no bugs or problems.
- 개발자들은 코드 작성에 있어 책임을 지지만, 다른 문제들에 있어서는 책임을 지지 않습니다.\
이것은 개발자들이 고품질의 코드를 사용자에게 제공하려 노력할 것이지만 만약 출석체크 누락 등의 문제가 생긴다면 개발자들은 이에 대해 어떠한 책임도 지지 않는다는 것을 의미합니다.\
특히 상기하였듯, GADC의 비공식 배포버전을 사용하여 중대한 문제들이 발생할 때 그것은 전적으로 사용자의 실책입니다.\
GADC이 완벽한 프로그램이 아니라는 것을 기억해주세요. 개발진은 GADC을 더 좋게 개선하려 노력할 것이지만, 이것이 아무런 버그나 문제가 없다는 것을 의미하지는 않습니다.

### About HoYoLAB
- GADC does not have any relations with HoYoLAB and MiHoYo.\
If you use this program and connect to HoYoLAB (or other sites managed by MiHoYo), that means you are agreeing to the sites' terms&policy.
- GADC은 호요랩과 미호요와 아무런 관계가 없습니다.\
만약 이 프로그램을 이용하여 호요랩이나 미호요에 의해 관리되는 다른 사이트에 접속할 경우, 이것은 당신이 해당 사이트의 정책에 동의한다는 것을 의미합니다.

### About MiHoYo's ban request
- If MiHoYo(or who delegated authority from MiHoYo) request to stop the distribution of the GADC, we will follow their request
because MiHoYo has whole permission that interprets this.
- MiHoYo가 이 프로그램의 해석에 대한 모든 권한을 가지고 있기 때문에,
만약 MiHoYo(또는 당사로부터 권한을 위임받은 자)가 GADC의 배포를 중단할 것을 요청한다면 우리는 해당 요청에 따를 것입니다.

-----

## 📜 Update History

> Version with 🛠️  mark means it's under developing

### Java Version

|Java|Details|
|:---:|:------------|
|1.0.0|Initial version of GADC|
|1.0.1|Add additional vbs messages|
||[Fix bugs] initial auto check-in, Clean up duplicated files in jar|
||[Change] Edit some sentences of vbs messages, Refactor codes|
|*1.0.2-beta*|[Fix bugs] Bug that GC process does not work normally after check-in|
|*1.0.3*🛠️|Add auto starting function|
|*1.1.0*🛠️|Supports auto update through GitHub release|
||[Fix bugs] Unknown bugs randomly caused at initial startup|


### Python Version

|Python|Details|
|:---:|:------------|
|*1.0.0*🛠️|Initial version of GADC|


## 🔌 Dependencies / Open-Source Libs / License
- Java 17
- Selenium 4.1.1 (Java / Python)
- Guava 31.0.1
- Logo image resources(Primogem, Pot) are provided by MiHoYo on the premise of using them for non-commercial purposes. MiHoYo All Rights Reserved.

## About Contribution
Anyone can contribute and open PR :)\
Let's develop this together~\
누구든 contribute 하고 PR을 열 수 있어요 :)\
같이 개발합시다 ㅎㅎ..\
\
*p.s. I cannot do C++.. If you can do C++, how abt joining this project?*

