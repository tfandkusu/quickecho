[![CircleCI](https://circleci.com/gh/tfandkusu/quickecho/tree/master.svg?style=svg)](https://circleci.com/gh/tfandkusu/quickecho/tree/master)

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

ボタンが押される等のユーザ操作が行われるとActionCreatorのメソッドが呼ばれます。ActionCreatorは処理の途中経過や結果をActionとして発行します。StoreはActionを受け取り、内容に応じてLiveDataを変更します。ActivityはLiveDataを監視し、内容に応じて表示や音声デバイス制御を行います。

このアプリはAPI呼び出しやファイル書き込みがまだ無いため、ActionCreatorがそれに使用するRepositoryとその具体的実装になるDataStoreがまだ無いです。

## メイン画面View層の設計

<img src="https://github.com/tfandkusu/quickecho/blob/master/images/MainActivity.png?raw=true">

このアプリは音声の録音再生、インタラクティブなエフェクトなどView層が大きいため、役割別ViewHelperに処理を分けています。

- RecordViewHelper 音声の録音
- PlayViewHelper 音声の再生
- SoundEffectViewHelper 効果音の再生
- VisualVolumeViewHelper 音量に合わせて大小が変わる円のエフェクト
- AnimatorViewHelper ボタンと中心にある状態表示の表示非表示アニメーション
- TimerViewHelper 最大録音時間の管理

RecordViewHelperとPlayViewHelperはメモリに音声を保存して再生時に読み出すために、お互いシングルインスタンスなSoundLocalDataStoreを持っています。

# 使用技術

- [Kotlin Coroutine](https://github.com/Kotlin/kotlinx.coroutines) 音声の録音と再生を除く非同期処理全般で使用しています。
- [Koin](https://insert-koin.io/) DIコンテナです。
- [MockK](https://mockk.io/) 単体テストの時に依存するクラスをモック化しています。
- [CircleCI](https://circleci.com/)
- [KotlinTest](https://github.com/kotlintest/kotlintest) 単体テストの時に `actual shouldBe expected` のように中間値表記で検証ができます。
- [Material Components](https://material.io/components/) [Floating Action Button](https://material.io/develop/android/components/floating-action-button/)と[CardView](https://material.io/develop/android/components/material-card-view/)の実装に使用しています。
- [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout.html)
- [EventBus](https://github.com/greenrobot/EventBus) FluxのDispatcherの実装に使っています。

# 今後の予定

- 録音音声を保存して再生する機能の追加
- リリースの自動化


