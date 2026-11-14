# 나예💕 D-day

삼성 갤럭시 잠금화면에서 **나예💕 · D+100**처럼 작고 투명한 날짜 카운터를 표시하는 앱입니다.

- 이름 / 기념일 / 분홍 또는 흰색 글자 지정
- 앱 자체 설정 화면과 실제 Android App Widget 포함
- D+, D-, D-DAY 자동 계산, 날짜 변경·자정 갱신 예약
- 네트워크·광고·추적 없음

## 📱 APK 직접 설치
[DDay.apk 최신 버전](https://github.com/ddogy1212/dday/releases/latest/download/DDay.apk)

설치 후 앱을 열어 이름, 기념일을 입력하고 저장합니다.

## 🔒 잠금화면에 배치
1. Galaxy Store에서 **Good Lock**, 그 안에서 **LockStar**를 설치합니다.
2. LockStar의 잠금화면 편집 → 위젯 추가 → **나예 D-day** 위젯을 선택합니다.
3. 크기를 조절해 시계 바로 아래에 놓고 저장합니다.

**제약:** 삼성 시스템이 일반 앱에 잠금화면 직접 편집 권한을 주지 않으므로 배치에는 LockStar가 필요합니다. One UI 버전/기종에 따라 타사 위젯 노출이 제한되거나 일시적인 오류가 있을 수 있습니다. 일부 기기는 AOD 위젯이 제한됩니다.

## 🛠 개발
순수 Java Android 앱(외부 라이브러리 없음); Android Studio 또는 Gradle 8.10.2 + Android SDK 35에서 `gradle :app:assembleDebug`를 실행합니다.

GitHub Actions가 main push마다 Android APK를 빌드해 Releases의 `DDay.apk`에 공개합니다. 디버그 서명 APK이므로 업데이트 시 서명 차이로 기존 버전을 제거해야 할 수 있습니다.
