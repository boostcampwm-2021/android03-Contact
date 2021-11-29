<h1 align="center">컨택</h1>

<p align="center">
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/boostcampwm-2021/android03-Contact/actions/workflows/develop-branch-review.yml"><img alt="API" src="https://github.com/boostcampwm-2021/android03-Contact/actions/workflows/develop-branch-review.yml/badge.svg"/></a>  
</p>

<p align="center">
ReadMe in <a href="https://github.com/boostcampwm-2021/android03-Contact/blob/develop/docs/ReadMeEn.md">English</a><br>
Contact는 안드로이드 기반의 지인 관리 애플리케이션입니다.
<br>

<p align="center">
<img src="https://user-images.githubusercontent.com/57510192/143352328-dece1dcf-60f6-4726-a161-3d8db1aa8576.png" width="75%" />
</p>

## Previews
<img src="https://user-images.githubusercontent.com/57510192/143807697-396a4e55-3175-48e8-86d7-81e01111fab6.gif" width="100%">

## 제공되는 기능
-   친구와 지인의 정보를 기록하고 저장할 수 있는  **컨택트**입니다.
-   여러분들의 소중한 파트너를 기록해보세요. 어떤 정보라도 좋습니다.
-   지인 정보를 기록함으로써 지인을 기억하고 다음 만남에서 더 자연스럽게 대화를 이어나갈 수 있도록 도와드릴게요.
-   이뿐만 아니라 지인과의 약속을 저장해주시면 기억하고 있다가 해당 약속을 알림으로 알려드릴게요.
-   컨택을 보호하기 위해 비밀번호와 지문인식 기능을 이용할 수 있어요. 걱정하지 마세요.

## 기술 스택
- Minimum SDK level 21로 98% 이상의 안드로이드 디바이스 지원
- 100% [Kotlin](https://kotlinlang.org/) 기반 + [Coroutines](https://developer.android.com/kotlin/coroutines) + [Flow](https://developer.android.com/kotlin/flow)
- [Jetpack](https://developer.android.com/jetpack)
  - ViewModel
  - Room Persistence
- Architecture
  - MVVM Architecture ( View - Databinding - ViewModel - Model )
  - Repository Pattern
- DI를 위한 [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- 지문인식을 위한 [Biometric](https://developer.android.com/jetpack/androidx/releases/biometric)
- 알림을 위한 [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- 이미지 처리를 위한 [Glide](https://github.com/bumptech/glide)
- 온보딩 애니메이션을 위한 [Lottie](https://airbnb.io/lottie/#/)
- Unit test를 위한 [JUnit 4](https://github.com/junit-team/junit4)
- CI를 위한 [Github Actions](https://github.com/boostcampwm-2021/android03-Contact/tree/develop/.github/workflows)
- 한국어, 영어, 중국어 지원

## 릴리즈
이번 겨울 구글 플레이 스토어에서 만나요.

## MAD Scorecard
<img src="https://user-images.githubusercontent.com/57510192/143381010-667f6493-b547-46f1-9e24-0d967c4cfd0b.png">
<img src="https://user-images.githubusercontent.com/57510192/143814020-43f3aaac-f21a-4b17-a166-e37121d29184.png">
<img src="https://user-images.githubusercontent.com/57510192/143814025-e21ff3cb-9eed-4e0c-ae23-87f9ea8d9d98.png">

## 연락처
email : bcivyclub@gmail.com