<img src="https://codebuild.ap-northeast-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiRy85TjhGUW4zOGwwZ3FqSkt3TzBNeEt6SFhGY2lCQ0liejBsaGJSY0U2UGtNWEtLRktGN0RYczJqODdSbDg5QkdCTDFtRWErS3llZ1pPR1B1cGFXbVJrPSIsIml2UGFyYW1ldGVyU3BlYyI6IlpSdjZ1WVdxM1hNemJLbSsiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master">

クイックエコー
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

このアプリはAPI呼び出しやファイル書き込みがまだ無いため、ActionCreatorがそれに使用するRepositoryとその具体的実装になるDataStoreがまだ無いです。(後述するView層に配置している録音や効果音機能から呼ばれるRepositoryはあります。)

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

DIコンテナの[Koin](https://insert-koin.io/)の設定を行います。
Activityを呼び出し[Jetpack Navigation](https://developer.android.com/guide/navigation)を構築します。
画面遷移のためのオブジェクトはDIを使い各機能に注入します。

### main

音声の録音と再生を行うメインとなるFragmentを担当します。

### setting

設定画面のFragmentを担当します。

### flux

画面構築のために共通で使われるクラスとリソースを提供します。

### repository

情報の出し入れを担当します。どこに情報があるかについては使用者は関与しません。

### localDataStore

端末内のメモリまたはファイルにある情報の出し入れを担当します。

# 使用技術

## アプリ

- [Kotlin Coroutine](https://github.com/Kotlin/kotlinx.coroutines) 音声の録音と再生を除く非同期処理全般で使用しています。
- [Koin](https://insert-koin.io/) DIコンテナです。
- [MockK](https://mockk.io/) 単体テストの時に依存するクラスをモック化しています。
- [KotlinTest](https://github.com/kotlintest/kotlintest) 単体テストの時に `actual shouldBe expected` のように中間値表記で検証ができます。
- [Material Components](https://material.io/components/) [Floating Action Button](https://material.io/develop/android/components/floating-action-button/)と[CardView](https://material.io/develop/android/components/material-card-view/)の実装に使用しています。
- [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout.html)
- [Jetpack Navigation](https://developer.android.com/guide/navigation)
- [Groupie](https://github.com/lisawray/groupie)
- [EventBus](https://github.com/greenrobot/EventBus) FluxのDispatcherの実装に使っています。
- [Flow Preferences](https://github.com/tfcporciuncula/flow-preferences/) 設定画面での効果音設定をメイン画面に反映させることに使っています。
- [Timber](https://github.com/JakeWharton/timber)
- [Flipper](https://fbflipper.com/)

## CI

- [AWS CodeBuild](https://aws.amazon.com/jp/codebuild/) PUSH毎の単体テストとlint結果投稿([Danger](https://github.com/danger/danger))を行います。
- [AWS Chatbot](https://aws.amazon.com/jp/chatbot/) CIの結果をSlackに投稿します。

## CD

- [Bitrise](https://www.bitrise.io/) Google Playへの自動公開と[Release](https://github.com/tfandkusu/quickecho/releases)の自動作成。
- [PyGithub](https://github.com/PyGithub/PyGithub) [Release](https://github.com/tfandkusu/quickecho/releases)の自動作成に使っています。

# 今後の予定

- 録音音声を保存して再生する機能の追加

