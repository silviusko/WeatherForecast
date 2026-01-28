# 天氣預報 App 實作計畫

本計畫概述了基於分析結果來建構天氣預報應用程式的步驟，以及最近重構工作的狀態。

## 使用者審閱 (已完成)
> [!IMPORTANT]
> **API Key**: 已收到並將配置於 `local.properties` (或 Build Config)。
> **依賴注入**: 確認使用 **Hilt**。
> **資料庫**: 使用 **Room**。
> **導航**: 使用 **Compose Navigation**。

---

## 實作變更與狀態

### 1. 專案初始化與配置 (已完成)
- **Gradle 配置**:
    - 解決插件版本與 JVM 目標相容性問題。
    - 加入 OpenWeatherMap API Key 註解說明。
- **Git**: 配置 `.gitignore` 確保忽略 `.idea` 等檔案。

### 2. 領域層 (Domain Layer) (已完成)
- **模型 (Models)**:
    - `City` (城市): `id`, `name` (API用), `localName` (顯示用), `country`, `lat`, `lon`。
    - `DailyForecast` (每日預報): 包含日期、溫度、天氣狀況、圖示。
    - `WeatherForecast` (天氣預報): 包含城市資訊、當前天氣、每週預報。
- **介面 (Interfaces)**:
    - `WeatherRepository`: `getWeatherForecast(city)`
    - `CityRepository`: `searchCities(query)`
- **使用案例 (Use Cases)**:
    - `GetWeatherForecastUseCase`
    - `SearchCitiesUseCase`

### 3. 資料層 (Data Layer) (已完成)
- **資料庫 (Room)**:
    - `AppDatabase`: 版本 2 (支援 `localName` 欄位)。
    - `CityEntity`: 包含 `id`, `name`, `localName` (新增), `country`, `lat`, `lon`。
    - `CityDao`: 提供本地城市搜尋與儲存。
    - **預先填充**: 首次啟動從 `city.list.min.json` (Asset) 匯入資料。
- **網路 (Network/Remote)**:
    - `WeatherApiService`: 包含 `getCoordinates` (Geocoding) 與 `getWeatherForecast`。
    - `GeocodingDto`: 支援 `local_names` 解析。
- **資料來源 (Data Sources)** (重構後):
    - `LocalCityDataSource`: 封裝 CityDao 與 Asset 初始化邏輯。
    - `RemoteCityDataSource`: 封裝 API 呼叫與多語系名稱解析邏輯 (`localName` 優先使用系統語言)。
- **Repository 實作**:
    - `CityRepositoryImpl`: 整合 Local 與 Remote 來源。
        - **搜尋邏輯**: 優先顯示 Remote 結果 (`Remote + Local`)。
        - **混合搜尋**: 同時查詢本地資料庫與遠端 API。

### 4. 表現層 (Presentation Layer) (已完成)
- **Clean Architecture 整合**:
    - `WeatherUiMapper`: 將 Domain 模型轉換為 UI 模型 (`WeatherUiModel`)，處理圖片 URL 邏輯。
- **ViewModels**:
    - `CityListViewModel`:
        - 使用 `flatMapLatest` 與 `StateFlow` 優化資料流。
        - **搜尋優化**: 限制最少 2 字元，Debounce 時間調整為 0.5 秒以節省 API 用量。
    - `WeatherViewModel`: 管理天氣資料載入狀態。
- **UI 畫面 (Jetpack Compose)**:
    - `LaunchScreen`:
        - 顯示載入中訊息 (支援多語系：「資料載入中，請稍後...」)。
        - 判斷是否已有儲存城市並導航。
    - `CityListScreen`:
        - 顯示搜尋結果，優先顯示 `localName` (例如：「臺北市」)。
    - `WeatherScreen`:
        - 玻璃擬態 (Glassmorphism) 設計風格。
        - 顯示當前天氣、每 3 小時預報 (水平捲動)、每週預報。
        - 標題顯示在地化城市名稱。
        - 導航欄位整合「設定」按鈕 (取代返回鍵) 以切換城市。

### 5. 導航流程 (Navigation) (已完成)
- `WeatherNavHost`:
    - **邏輯優化**:
        - 首次啟動 -> `Launch` -> `CityList`。
        - 已有城市 -> `Launch` -> `Weather`。
        - 選擇城市 -> 清除 Back Stack -> `Weather` (Root)。
        - 點擊設定 -> Push `CityList` -> 可返回 `Weather`。

---

## 驗證計畫

### 自動化測試 (重點: Domain & Data Layers)
- **工具**: JUnit 5, MockK。
- **測試範圍**:
    - UseCases 邏輯。
    - Repository 整合邏輯 (Data Sources)。
    - Mapper 資料轉換。
    - **注意**: UI 自動化測試暫不執行。

### 手動驗證項目
1.  **搜尋體驗**:
    - 輸入少於 2 字不觸發搜尋。
    - 輸入後等待 0.5 秒才觸發 API。
    - 確認顯示繁體中文名稱 (若系統語言為中文)。
2.  **資料優先權**:
    - 搜尋結果列表中，確認 Remote 來源 (通常有中文名) 排在 Local 來源之前 (若有重複)。
3.  **導航流程**:
    - 首次安裝流程 vs. 重啟流程。
    - 從 Weather 頁面切換城市的 Back Stack 行為。
4.  **載入畫面**:
    - 首次啟動時確認看到「資料載入中」提示。
