package cn.sk.mask.sys.custom.tag;

import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import cn.sk.mask.sys.pojo.SysSqlConfCustom;
import cn.sk.mask.sys.pojo.SysSqlConfQueryVo;
import cn.sk.mask.sys.service.ISysDictService;
import cn.sk.mask.sys.service.ISysSqlConfService;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.common.ServerResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Slf4j
public class SelectTag extends BaseTag {
    //标签名
    private static final String TAG_NAME  = "select";
    //html标签名
    private static final String HTML_TAG_NAME  = "select";
    //是否是单标签
    private static final boolean IS_SINGLE_TAG  = Boolean.FALSE;
    //优先级
    private static final int PRECEDENCE = 10000;
    private static StringBuilder attrHtml = new StringBuilder();
    //特有的类样式
    private static String subsClazz = "select";
    //大小
    private String size;
    private String dictType;//字典类型
    //语句编码
    private String scCode;

    private ISysDictService sysDictService;
    private ISysSqlConfService sysSqlConfService;
    private JdbcTemplate jdbcTemplate;

    public SelectTag(String dialectPrefix) {
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)

    }
    public SelectTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        //要调用，不能去除
        super.doProcess(context, tag, structureHandler);

        sysDictService = appCtx.getBean(ISysDictService.class);
        sysSqlConfService = appCtx.getBean(ISysSqlConfService.class);
        jdbcTemplate = appCtx.getBean(JdbcTemplate.class);

        /*
         * 指示引擎用指定的模型替换整个元素。
         */
//        structureHandler.replaceWith(model, false);
        String html = this.genHtml();
//        log.debug(TAG_NAME+":{}",html);
        structureHandler.replaceWith(html, false);

    }

    @Override
    protected void setAttributeValue(IProcessableElementTag tag) {
        //要调用，不能去除
        super.setAttributeValue(tag);
        attrHtml.delete(0,attrHtml.length());

        String size = tag.getAttributeValue("size");
        if(!StringUtils.isEmpty(size)) {
            this.setSize(size);
        }else{
            this.setSize("1");
        }
        attrHtml.append(" size=\"");
        attrHtml.append(this.getSize());
        attrHtml.append("\"");

        String dictType = tag.getAttributeValue("dictType");
        this.setDictType(dictType);

        String scCode = tag.getAttributeValue("scCode");
        this.setScCode(scCode);

    }

    @Override
    protected String getSubsClazz() {
        return subsClazz;
    }

    //生成html
    private String genHtml(){
        StringBuilder html = new StringBuilder();

        html.append("<");
        html.append(HTML_TAG_NAME);
        //获取基本的属性值
        html.append(this.getBaseAttrHtml());
        html.append(attrHtml.toString());

        if (IS_SINGLE_TAG){
            html.append("/>");
        }else {
            html.append(">");
            html.append("<option value=\"\">");
            html.append("----请选择----");
            html.append("</option>");
            //根据数据生成
            if(!StringUtils.isEmpty(this.dictType)) {
                html.append(this.genDataByDictType());
            }else if(!StringUtils.isEmpty(this.scCode)) {
                html.append(this.genDataByScCode());
            }

            html.append("</");
            html.append(HTML_TAG_NAME);
            html.append(">");
        }


        return html.toString();
    }

    //根据字典类型生成数据
    private String genDataByDictType(){
        StringBuilder html = new StringBuilder();

        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();

        SysDictCustom condition = new SysDictCustom();
        condition.setDictType(dictType);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.getIsNoLike().put("dictType",true);
        sysDictQueryVo.setOrderBy(" sort asc");
        sysDictQueryVo.setSysDictCustom(condition);

        ServerResponse<List<SysDictCustom>> sr = sysDictService.queryObjs(sysDictQueryVo);
        List<SysDictCustom> data = sr.getData();
        for (int i = 0,len = data.size(); i < len; i++){
            SysDictCustom s = data.get(i);
            html.append("<option value=\"");
            String dictCode = s.getDictCode();
            html.append(dictCode);
            html.append("\" ");
            //判断是否有值
            String value = this.getValue();
            if(!StringUtils.isEmpty(value)&&StringUtils.equals(value,dictCode)) {
                html.append("selected ");
            }
            html.append(">");
            html.append(s.getCodeName());
            html.append("</option>");
        }
        return html.toString();
    }
    //根据语句编码生成数据
    private String genDataByScCode(){
        StringBuilder html = new StringBuilder();
        SysSqlConfQueryVo sysSqlConfQueryVo = new SysSqlConfQueryVo();

        SysSqlConfCustom sysSqlConfCustom = new SysSqlConfCustom();
        sysSqlConfCustom.setScCode(this.getScCode());
        sysSqlConfCustom.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysSqlConfQueryVo.setSysSqlConfCustom(sysSqlConfCustom);
        ServerResponse<List<SysSqlConfCustom>> serverResponse = sysSqlConfService.queryObjs(sysSqlConfQueryVo);
        List<SysSqlConfCustom> sysSqlConfCustoms = serverResponse.getData();
        if(CollectionUtils.isEmpty(sysSqlConfCustoms)) {
            log.error("sql语句没有配置:语句码为{}",sysSqlConfCustom.getScCode());
            return null;
        }
        String sql = sysSqlConfCustoms.get(0).getScStatement();

        try {
            List<Map<String,Object>> data = jdbcTemplate.queryForList(sql);
            for(int i = 0,len = data.size(); i < len; i++) {
                Map<String,Object> item = data.get(i);
                html.append("<option value=\"");
                String dictCode = item.get("code").toString();
                html.append(dictCode);
                html.append("\" ");
                //判断是否有值
                String value = this.getValue();
                if(!StringUtils.isEmpty(value)&&StringUtils.equals(value,dictCode)) {
                    html.append("selected ");
                }
                html.append(">");
                html.append(item.get("name").toString());
                html.append("</option>");
            }

        }catch (DataAccessException e){
            log.error("sql语句配置错误:语句码为{}",sysSqlConfCustom.getScCode());
        }

        return html.toString();
    }

}
