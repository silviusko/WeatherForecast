# WeatherForecast

這是一個現代化的 Android 天氣預報應用程式，採用 **Clean Architecture** 架構與現代 Android 開發技術堆疊 (Jetpack Compose, Hilt, Room, Retrofit) 構建。

## 📱 應用程式簡介

WeatherForecast App 提供使用者查詢全球城市天氣的功能，並具備以下特色：

*   **實時天氣查詢**：串接 OpenWeatherMap API，提供即時的溫度、天氣狀況、最高/最低溫等資訊。
*   **未來預報**：提供未來 5 天且每 3 小時的天氣預報趨勢。
*   **離線搜尋支援**：內建全球城市資料庫 (Room)，支援離線關鍵字搜尋城市。
*   **常用城市儲存**：可將查詢後的城市設為常用，下次開啟 App 自動載入。
*   **多國語系**：完整支援**繁體中文 (Traditional Chinese)** 與 **英文 (English)**，並根據系統語系自動切換 API 回傳資料的語言。
*   **優雅的 UI/UX**：使用 Jetpack Compose 打造的 Material Design 3 介面，支援動態載入與錯誤處理。

## 🏗️ 專案架構 (Modular Architecture)

本專案採用 **Multi-Module Gradle** 架構，以落實高度解耦與關注點分離：

*   **`:domain`**：純 Kotlin 模組，包含業務邏輯（Models, Use Cases, Repository Interfaces）。不依賴任何 Android SDK。
*   **`:data`**：Android Library 模組，負責資料處理（Room Database, Retrofit API, Repositories 實作）。
*   **`:app`**：Android Application 模組，負責 UI 呈現（Jetpack Compose, ViewModels）與 Hilt 依賴注入的根配置。

## 🛠 技術堆疊 (Tech Stack)

*   **語言**: Kotlin
*   **架構**: Clean Architecture + MVVM + Modularization
*   **UI**: Jetpack Compose, Material3
*   **依賴注入**: Hilt
*   **網路**: Retrofit, OkHttp, Gson
*   **資料庫**: Room Database
*   **非同步處理**: Coroutines, Flow
*   **測試**: JUnit 5, MockK

## 🚀 快速開始 (Setup)

1.  **取得 API Key**：請至 [OpenWeatherMap](https://openweathermap.org/api) 申請免費的 API Key。
2.  **配置 Key**：在專案根目錄的 `local.properties` 檔案中加入以下設定：
    ```properties
    OPEN_WEATHER_API_KEY=您的_API_KEY
    ```
3.  **編譯執行**：使用 Android Studio 開啟專案並執行 `:app` 模組。


## 📂 專案文件 (Documentation)

*   [requirements.md](document/requirements.md) - 專案原始需求說明 (Assignment Requirements)。
*   [implementation_plan.md](document/implementation_plan.md) - 實作計畫與完成狀態 (Implementation Plan & Status)。
*   [ui_design.md](document/ui_design.md) - UI 設計規範與參考 (UI Design Guidelines)。

## 🤖 關於 Antigravity 的協作開發

本專案是透過 **Antigravity (Agentic AI)** 協助開發的成果。Antigravity 是一個強大的 AI 代理系統，在本專案中扮演了「結對程式設計師 (Pair Programmer)」的角色。

### Antigravity 是如何協助建立這個 App 的？

1.  **架構規劃 (Architecture Planning)**：
    *   Antigravity 首先分析需求，產出了完整的 `implementation_plan.md`，定義了 Clean Architecture 的分層結構、資料模型與必要的 Use Cases。

2.  **自動化實作 (Agentic Implementation)**：
    *   從 Domain Layer 的實體與邏輯，到 Data Layer 的 API 與資料庫整合，再到 Presentation Layer 的 UI 實作，Antigravity 能夠理解上下文並自動編寫程式碼檔案。

3.  **單元測試與品質保證 (Testing & QA)**：
    *   Antigravity 主動撰寫單元測試 (Unit Tests)，協助專案在 Domain 與 Data 層達到高標準的測試品質。在遇到測試失敗或 hang 住的情況時，亦能自動分析並修復問題。

4.  **重構與優化 (Refactoring)**：
    *   專案過程中，Antigravity 協助進行了多次重構，例如將 `java.time` API 替換為舊版 API 以相容舊 Android 版本，以及優化 Hilt 模組配置 (使用 `@Binds` 替代 `@Provides`)。

5.  **國際化支援 (Localization)**：
    *   Antigravity 自動掃描 UI 中的硬編碼字串，提取至 `strings.xml`，並建立了繁體中文的資源檔，甚至修改了 API 呼叫邏輯以支援動態語言參數。

6.  **功能擴充與優化 (Feature Expansion & Optimization)**：
    *   整合 **OpenWeatherMap Geocoding API** 實現混合城市搜尋 (Local + Remote)，並實作優先邏輯以遠端資料為主。
    *   重構資料層，拆分為 `LocalDataSource` 與 `RemoteDataSource`，嚴格遵守 Clean Architecture。
    *   實作搜尋優化 (0.5s debounce, 限制最少 2 字元) 以有效管理 API 用量。
    *   透過 **Glassmorphism**、向量圖示與動態導航流程提升 UI/UX 體驗。

透過 Antigravity，開發者可以專注於高層次的需求與決策，而將繁瑣的實作細節、測試撰寫與除錯工作交由 AI 代理高效完成。