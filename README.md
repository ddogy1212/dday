# 나예💕 D-day — 삼성 잠금화면용 미니 위젯

시계 바로 아래에 **나예💕 · D+100**처럼 이름과 D-day만 표시하도록 만든 아주 작은 안드로이드 위젯입니다.

- 앱에서 이름, 기념일, 분홍색/흰색 글자 설정
- 기념일 기준으로 D+, D-, D-DAY 자동 계산
- 광고, 네트워크 권한, 수집 기능 없음
- 홈 화면 위젯 + 삼성 Good Lock의 LockStar 잠금화면 위젯 지원(기기/One UI에 따라 차이)

## 설치
[최신 APK 내려받기](https://github.com/ddogy1212/dday/releases/latest/download/DDay.apk)

1. APK를 설치하고 앱을 열어 이름과 기념일을 지정한 후 **저장**합니다.
2. **Galaxy Store → Good Lock → LockStar** 설치.
3. LockStar에서 잠금화면 편집 → **위젯 추가** → **나예 D-day** 선택.
4. 시계 바로 아래에 배치하고 저장합니다.

**참고:** 잠금화면 배치는 LockStar에서 합니다. 앱 자체에서 잠금화면을 직접 수정하지 않습니다. 일부 One UI 버전에서 서드파티 위젯을 표시하지 못할 수 있습니다.

## 개발자용 빌드
Android Studio로 프로젝트를 열고 Gradle Sync 후 `assembleDebug`를 빌드하세요.

GitHub Actions가 main 브랜치에 push될 때 APK를 빌드하고 공개 GitHub Release에 `DDay.apk`로 업로드합니다.
