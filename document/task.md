# 任務列表 (Tasks)

## 1. 專案初始化與設置 (Project Initialization & Setup)
- [x] 設定 `build.gradle.kts` (依賴庫: Retrofit, Room, Coil, Hilt, Navigation, Coroutines)。
- [x] 建立 Clean Architecture 套件結構 (domain, data, presentation)。
- [x] 設定 API Key 管理方式。
- [x] Git 配置 (.gitignore)。

## 2. 領域層實作 (Domain Layer Implementation)
- [x] 定義資料模型 (Models): `City`, `DailyForecast`, `WeatherForecast`。
- [x] 定義 Repository 介面: `WeatherRepository`, `CityRepository`。
- [x] 實作使用案例 (Use Cases): `GetWeatherForecastUseCase`, `SearchCitiesUseCase`。

## 3. 資料層實作 (Data Layer Implementation)
- [x] **網路 (Network)**:
    - [x] 定義 `WeatherApiService` (Retrofit)。
    - [x] 建立 DTOs 與 Mappers。
    - [x] 整合 **Geocoding API** (`RemoteCityDataSource`)。
- [x] **資料庫 (Database)**:
    - [x] 定義 `CityEntity`, `CityDao` (Room)。
    - [x] 設定 `AppDatabase` (Version 2, Migration)。
    - [x] 資料預先填充 (`LocalCityDataSource` 從 Asset 匯入)。
- [x] **Repositories 整合**:
    - [x] 實作 `WeatherRepositoryImpl`。
    - [x] 實作 `CityRepositoryImpl`: 整合 Local + Remote 混合搜尋優先邏輯。

## 4. 表現層實作 (Presentation Layer Implementation)
- [x] **UI 架構**:
    - [x] Goompose Theme, Glassmorphism 設計。
    - [x] ViewModels (`CityListViewModel`, `WeatherViewModel`)。
- [x] **畫面 (Screens)**:
    - [x] `LaunchScreen`: 載入狀態與導航判斷。
    - [x] `CityListScreen`: 城市搜尋與列表 (支援 `localName` 顯示)。
    - [x] `WeatherScreen`: 當前天氣、每 3 小時預報 (水平捲動)、每週預報。
- [x] **導航 (Navigation)**:
    - [x] 完善路由邏輯 (`Launch` -> `CityList` / `Weather`)。
    - [x] 整合設定按鈕與 Back Stack 處理。

## 5. 優化與擴充 (Optimization & Expansion)
- [x] **搜尋優化**:
    - [x] Debounce 時間調整為 0.5 秒。
    - [x] 限制最少 2 字元觸發搜尋。
- [x] **國際化 (Localization)**:
    - [x] 支援繁體中文 (Traditional Chinese) 與 英文。
    - [x] API 呼叫帶入對應語系參數。
    - [x] 城市名稱優先顯示在地化名稱 (`localName`)。
- [x] **UI/UX 優化**:
    - [x] 使用 Vector Icons 取代舊圖示。
    - [x] 載入畫面文字提示 ("資料載入中...")。

## 6. 文件 (Documentation)
- [x] 更新 `README.md` (整合 AI 工具說明與功能擴充)。
- [x] 更新 `implementation_plan.md`。
- [x] 更新 `ui_design.md`。
