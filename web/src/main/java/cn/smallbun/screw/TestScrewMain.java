package cn.smallbun.screw;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author zhenxu
 * @date 2023/2/9 14:40
 */
public class TestScrewMain {

    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:39000";
    private static final String DB_NAME = "db_datastory_channel?targetServerType=preferPrimary&useUnicode=true&characterEncoding=utf-8";
    private static final String DB_USERNAME = "api_rw";
    private static final String DB_PASSWORD = "VynbuetThDBQXcGs";
    private static final String FILE_OUTPUT_DIR = "/Users/zhenxu.liao/Desktop/";

    // 可以设置 Word 或者 Markdown 格式
    private static final EngineFileType FILE_OUTPUT_TYPE = EngineFileType.WORD;
    private static final String DOC_FILE_NAME = "数据库表设计文档";
    private static final String DOC_VERSION = "V1.0.0";
    private static final String DOC_DESCRIPTION = "数据库表设计描述";

    public static void main(String[] args) {
        // 创建 screw 的配置
        Configuration config = Configuration.builder()
                // 版本
                .version(DOC_VERSION)
                // 描述
                .description(DOC_DESCRIPTION)
                // 数据源
                .dataSource(buildDataSource())
                // 引擎配置
                .engineConfig(buildEngineConfig())
                // 处理配置
                .produceConfig(buildProcessConfig())
                .build();

        // 执行 screw，生成数据库文档
        new DocumentationExecute(config).execute();
    }

    /**
     * 创建数据源
     */
    private static DataSource buildDataSource() {
        // 创建 HikariConfig 配置类
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl(DB_URL + "/" + DB_NAME);
        hikariConfig.setUsername(DB_USERNAME);
        hikariConfig.setPassword(DB_PASSWORD);
        // 设置可以获取 tables remarks 信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        // 创建数据源
        return new HikariDataSource(hikariConfig);
    }

    /**
     * 创建 screw 的引擎配置
     */
    private static EngineConfig buildEngineConfig() {
        return EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(FILE_OUTPUT_DIR)
                // 打开目录
                .openOutputDir(true)
                // 文件类型
                .fileType(FILE_OUTPUT_TYPE)
                // 文件类型
                .produceType(EngineTemplateType.freemarker)
                // 自定义文件名称
                .fileName(DOC_FILE_NAME)
                .build();
    }

    /**
     * 创建 screw 的处理配置，一般可忽略
     * 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
     */
    private static ProcessConfig buildProcessConfig() {
        return ProcessConfig.builder()
                // 根据名称指定表生成
                .designatedTableName(Arrays.asList("access_key","access_key_conf","adcode","app_candidate_area","app_category_competitor_rel","app_form_conf","app_form_data","app_form_template","app_survey_area_owner","app_survey_area_owner_user_rel","app_survey_task","app_user_intention_info","authority_enterprise_poi_resource_record","authority_enterprise_policy_rel","authority_poi_resource_record","authority_policy","authority_uc_mapping","authority_user_policy_rel","candidate_area_mark_record","dict_adcode","dict_adcode_ctf","dict_industry_category","dict_o2o_platform","dict_profile_label","dict_tlbs_field_mapping","dim_attribute","dim_attribute_enum","dim_attribute_scope","dim_city","dim_grid_city_heat_relation","dim_industry_category","dim_industry_category_report_conf","dim_map_poi_field_desc","form_config","front_conf","industry_category_attribute_filter_conf","industry_category_scene_config","label","label_logo","layer_style_conf","map_amap_conf","map_aoi","map_aoi_attribute_his","map_aoi_evaluate_report","map_business_aoi_type","map_business_aoi_type_group","map_business_store_sales","map_category_attribute_logo_rel","map_category_logo_conf","map_city_model_index","map_city_yearbook","map_contrast_analysis_condition","map_contrast_analysis_selector_conf","map_country_category_overview_conf","map_country_category_ranking_conf","map_crowd","map_crowd_analysis_task","map_crowd_analysis_task_rel","map_customize_condition","map_enterprise_crowd_analysis_model","map_enterprise_evaluate_count","map_evaluate_datasource_conf","map_evaluate_enterprise_model_rel","map_evaluate_lbs_record_rel","map_evaluate_model_conf","map_evaluate_record","map_evaluate_report","map_grid","map_grid_heat_conf","map_grid_heat_import_record","map_grid_heat_visible_conf","map_grid_num_statistic","map_grid_type","map_industry_model_index","map_interest_area","map_interest_area_rel","map_mall_dashboard_tgi","map_o2o_store","map_open_space_model_score","map_poi","map_poi_city_decimation","map_poi_collection","map_poi_country_decimation","map_poi_logo_conf","map_poi_num_statistic","map_poi_origin","map_poi_origin_v2","map_poi_revise_record","map_potential_index_indicator_selector_conf","map_profile_indicator_selector_conf","map_style_conf","map_survey_task_package","map_table_attribute_conf","map_tlbs_record","map_user_city_record","map_user_evaluate_count","map_yearbook_indicator_conf","panel_conf","panel_conf_tab","poi_cluster","spatial_ref_sys","statistics_industry_category_conf","sys_amap_invoke_log","sys_async_task","sys_data_sync_record","sys_quartz_task","sys_quartz_task_record"))
                // 根据表前缀生成
                .designatedTablePrefix(Collections.<String>emptyList())
                // 根据表后缀生成
                .designatedTableSuffix(Collections.<String>emptyList())
                // 忽略表名
//                .ignoreTableName(Arrays.asList("test", "mytable","role","t_role","t_user"))
                 //忽略表前缀
                .ignoreTablePrefix(Arrays.asList("pg_", "tmp_"))
                // 忽略表后缀
                //.ignoreTableSuffix(Collections.singletonList("_test"))
                .build();
    }
}
