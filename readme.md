【開発停止中】クイックエコー
====
音声を録音して、素早く再生が出来るAndroidアプリです。英語の発音確認におすすめです。

# アプリ公開URL

https://play.google.com/store/apps/details?id=jp.bellware.echo

# アプリ詳細

録音音声が小さい場合も適切なボリュームに変換して再生します。

ボタン操作、音声の録音再生に対して、インタラクティブなエフェクトを実装しています。

<img src="https://github.com/tfandkusu/quickecho/blob/master/images/interactive.gif?raw=true" width="320px">

# 内部設計

[DroidKaigi/conference-app-2019](https://github.com/DroidKaigi/conference-app-2019)を参考にしたFlux設計になっています。

<img src="https://github.com/tfandkusu/quickecho/blob/master/images/flux.png?raw=true">

ボタンが押される等のユーザ操作が行われるとActionCreatorのメソッドが呼ばれます。ActionCreatorは処理の途中経過や結果をActionとして発行します。StoreはActionを受け取り、内容に応じてLiveDataを変更します。FragmentはLiveDataを監視し、内容に応じて表示や音声デバイス制御を行います。
データの入出力はRepositoryが担当し、Repositoryが必要に応じてローカルファイル保存のLocalDataStore、API呼び出しのRemoteDataStore(まだ無い)
を呼び出します。

## メイン画面View層の設計

<img src="https://github.com/tfandkusu/quickecho/blob/master/images/MainFragment.png?raw=true">

このアプリは音声の録音再生、インタラクティブなエフェクトなどView層が大きいため、役割別ViewHelperに処理を分けています。

- RecordViewHelper 音声の録音
- PlayViewHelper 音声の再生
- SoundEffectViewHelper 効果音の再生
- VisualVolumeViewHelper 音量に合わせて大小が変わる円のエフェクト
- AnimatorViewHelper ボタンと中心にある状態表示の表示非表示アニメーション
- TimerViewHelper 最大録音時間の管理

RecordViewHelperとPlayViewHelperはメモリに音声を保存して再生時に読み出すために、お互いシングルインスタンスなSoundRepositoryを持っています。

## マルチモジュール

<img src="https://github.com/tfandkusu/quickecho/blob/master/images/MultiModule.png?raw=true">

このアプリは機能別とレイヤー別のマルチモジュールが採用されています。

### app

アプリケーションモジュールです。DI(Dagger Hilt)の設定を行っています。

### main

音声の録音と再生を行うメインとなる画面を担当します。

### setting

設定画面を担当します。

### flux

画面構築のために共通で使われるクラスとリソースを提供します。

### repository

情報の出し入れを担当します。どこに情報があるかについては使用者は関与しません。

### localDataStore

端末内のメモリまたはファイルにある情報の出し入れを担当します。

# 使用技術

## アプリ

- [Kotlin Coroutine](https://github.com/Kotlin/kotlinx.coroutines) 音声の録音と再生を除く非同期処理全般で使用しています。
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja)
  DIコンテナです。
- [Koin](https://insert-koin.io/) Hiltの前に使っていたDIコンテナです。Hiltは検証中のためコード上は残しています。
- [MockK](https://mockk.io/) 単体テストの時に依存するクラスをモック化しています。
- [KotlinTest](https://github.com/kotlintest/kotlintest) 単体テストの時に `actual shouldBe expected`
  のように中間値表記で検証ができます。
- [Material Components](https://material.io/components/) [Floating Action Button](https://material.io/develop/android/components/floating-action-button/)と[CardView](https://material.io/develop/android/components/material-card-view/)の実装に使用しています。
- [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout.html)
- [Jetpack Navigation](https://developer.android.com/guide/navigation)
- [Groupie](https://github.com/lisawray/groupie)
- [Room](https://developer.android.com/topic/libraries/architecture/room?hl=ja)
  開発中の録音音声を保存する機能で使用しています。
- [EventBus](https://github.com/greenrobot/EventBus) FluxのDispatcherの実装に使っています。
- [Flow Preferences](https://github.com/tfcporciuncula/flow-preferences/)
  設定画面での効果音設定をメイン画面に反映させることに使っています。
- [Timber](https://github.com/JakeWharton/timber)
- [Flipper](https://fbflipper.com/)

## ビルド

- [Spotless plugin for Gradle](https://github.com/diffplug/spotless/tree/main/plugin-gradle)
- [Gradle DeployGate Plugin](https://github.com/DeployGate/gradle-deploygate-plugin)

## CI

- [Github Actions](https://docs.github.com/ja/actions)
  PUSH毎に単体テスト、lint結果投稿([Danger](https://github.com/danger/danger))、フォーマット確認を行います。stagingブランチにPUSHするとDeployGateで配布します。
- [Firebase Test Lab](https://firebase.google.com/docs/test-lab) SQLiteデータベース書き込みの単体テストに使用しています。

## CD

- [Bitrise](https://www.bitrise.io/) Google
  Playへの自動公開と[Release](https://github.com/tfandkusu/quickecho/releases)の自動作成。
- [PyGithub](https://github.com/PyGithub/PyGithub) [Release](https://github.com/tfandkusu/quickecho/releases)の自動作成に使っています。

## その他

- [Dependabot Preview](https://github.com/marketplace/dependabot-preview)

# 今後の予定

## 録音音声を保存して再生する機能の追加

[デザイン(Adobe XD)](https://xd.adobe.com/view/1ba4de3f-a856-4c29-89c3-29341ed1f598-4ef7/)

- **ステップ1** Roomに保存する
- **ステップ2** Firestoreに保存する
