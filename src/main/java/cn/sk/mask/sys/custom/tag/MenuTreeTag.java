package cn.sk.mask.sys.custom.tag;

import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.pojo.SysSqlConfCustom;
import cn.sk.mask.sys.pojo.SysSqlConfQueryVo;
import cn.sk.mask.sys.service.ISysSqlConfService;
import cn.sk.mask.sys.utils.JackJsonUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
public class MenuTreeTag extends BaseTag {
    //标签名
    private static final String TAG_NAME  = "menuTree";
    //html标签名
    private static final String HTML_TAG_NAME  = "ul";
    //是否是单标签
    private static final boolean IS_SINGLE_TAG = Boolean.FALSE;
    private static final String TYPE_CHECKBOX = "checkbox";
    private static final String TYPE_RADIO = "radio";
    //优先级
    private static final int PRECEDENCE = 10000;
    private static StringBuilder attrHtml = new StringBuilder();

    //特有的类样式
    private static String subsClazz = "ztree";
    //类型 checkbox,radio
    private String type;
    //父节点是否可选
    private boolean parentCheck = Boolean.TRUE;
    //父节点不可选提示信息
    private String parentNoCheckMsg = "父节点不能选择";
    //语句编码
    private String scCode;
    //是否有div做外框
    private boolean hasOut = Boolean.TRUE;
    //是否初始化table
    private boolean isInitTable = Boolean.TRUE;
    //是否使用URL（获取别的系统的数据的时候使用）
//    private boolean useUrl = Boolean.FALSE;


    //树形选择后表单提交的元素id
    private String inputValId = "_sk_"+System.nanoTime();

//    {id:5, pId:0, name:"广东省", open:true}
//    {id:4, pId:0, name:"河北省", open:true, nocheck:true},
    private ISysSqlConfService sysSqlConfService;
    private JdbcTemplate jdbcTemplate;

    public MenuTreeTag(String dialectPrefix) {
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)

    }
    public MenuTreeTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        //要调用，不能去除
        super.doProcess(context, tag, structureHandler);
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

        //初始属性值
        this.initAttributeValue();

        //清除数据
        attrHtml.delete(0,attrHtml.length());

        //语句编码
        String scCode = tag.getAttributeValue("scCode");
        if(!StringUtils.isEmpty(scCode)) {
            this.setScCode(scCode);
        }

        //是否有div做外框
        String hasOut = tag.getAttributeValue("hasOut");
        if(!StringUtils.isEmpty(hasOut)) {
            this.setHasOut(Boolean.valueOf(hasOut));
        }

        //是否初始化table
        String isInitTable = tag.getAttributeValue("isInitTable");
        if(!StringUtils.isEmpty(isInitTable)) {
            this.setInitTable(Boolean.valueOf(isInitTable));
        }

        //类型
        String type = tag.getAttributeValue("type");
        if(!StringUtils.isEmpty(type)) {
            this.setType(type);
        }

        //父节点是否可选
        String parentCheck = tag.getAttributeValue("parentCheck");
        if(!StringUtils.isEmpty(parentCheck)) {
            this.setParentCheck(Boolean.valueOf(parentCheck));
        }

        //父节点不可选提示信息
        String parentNoCheckMsg = tag.getAttributeValue("parentNoCheckMsg");
        if(!StringUtils.isEmpty(parentNoCheckMsg)) {
            this.setParentNoCheckMsg(parentNoCheckMsg);
        }

    }

    @Override
    protected String getSubsClazz() {
        return subsClazz;
    }

    //初始化属性值
    private void initAttributeValue(){
        //语句编码
        this.setScCode("");
        //是否有div做外框
        this.setHasOut(Boolean.TRUE);
        //是否初始化table
        this.setInitTable(Boolean.TRUE);

        this.setType("");
        //父节点是否可选
        this.setParentCheck(Boolean.TRUE);
        //父节点不可选提示信息
        this.setParentNoCheckMsg(parentNoCheckMsg);
        //语句编码
        this.setScCode("");
    }

    //生成html
    private String genHtml(){
        StringBuilder html = new StringBuilder();

        //判断id是否为空
        if(StringUtils.isEmpty(this.getId())) {
            this.setId("_sk_"+System.nanoTime());
        }


        //外框开始
        if(this.isHasOut()) {
            html.append("<div class=\"pos-a skMenuTreeOut\">");
        }

        //树形根节点的id
        String inputRootId = this.getId()+"RootId";
        if(!this.isInitTable) {
            //表单提交的值
            html.append("<input id=\"");
            html.append(this.inputValId);
            html.append("\"");
            html.append(" type=\"hidden\" name=\"");
            html.append(this.getName());
            html.append("\"");
            //判断是否有值
            String value = this.getValue();
            if(!StringUtils.isEmpty(value)) {
                html.append(" value=\"");
                html.append(value);
                html.append("\"");
            }
            html.append(" />");
        }else{
            //添加隐藏域
            html.append("<input type=\"hidden\" id=\"");
            html.append(inputRootId);
            html.append("\" value=\"\"/>");
        }

        html.append("<");
        html.append(HTML_TAG_NAME);
        //获取基本的属性值
        html.append(this.getBaseAttrHtml());
        html.append(attrHtml.toString());
        if (IS_SINGLE_TAG){
            html.append("/>");
        }else {
            html.append(">");

            html.append("</");
            html.append(HTML_TAG_NAME);
            html.append(">");
        }
        //外框结束
        if(this.isHasOut()) {
            html.append("</div>");
        }

        //获取js
        html.append(this.genJs(inputRootId));
//        System.out.println(this.genJs());
        return html.toString();
    }
    //生成js
    private String genJs(String inputRootId) {
        StringBuilder js = new StringBuilder();
        js.append("<script type=\"text/javascript\">");

        if(this.isInitTable) {
            //tree 的 setting
            //tree初始化数据的引用名
            String initSettingName = "window."+this.getId()+"Setting";
            js.append(initSettingName);
            js.append(" = {");

            //异步
            js.append("async: { enable: true,},");

            js.append("view: {dblClickExpand: false},data: {simpleData: {enable: true}},");
            js.append("callback: {");
            js.append("beforeClick: beforeClick,");
            js.append("}};");

            //添加function
            //beforeClick
            js.append("function beforeClick(treeId, treeNode) {");
            js.append("$(\"#");
            js.append(inputRootId);
            js.append("\").attr(\"value\", treeNode.id);");
            js.append("if (treeNode.isParent) {drawTable();return false;}");
            js.append("else {return true;}}");



            js.append("$(document).ready(function(){");

            //tree初始化数据的引用名
            String initDataName = "window."+this.getId()+"InitData";

            //获取数据
            js.append(initDataName);
            js.append("=");
            if(!StringUtils.isEmpty(this.scCode)) {
                js.append(this.genData());
            }else{
                js.append("[]");
            }
            js.append(";");

            //tree初始化
            js.append("$.fn.zTree.init($(\"#");
            js.append(this.getId());
            js.append("\"), ");
            js.append(initSettingName);
            js.append(", ");
            js.append(initDataName);
            js.append(");");

            if(this.isInitTable) {
                //设置根节点di
                js.append("var treeObj = $.fn.zTree.getZTreeObj(\"");
                js.append(this.getId());
                js.append("\");var node = treeObj.getNodesByFilter(function (node) { return node.level == 0 }, true);");
                js.append("if(!node||!node.id){return}");
                js.append("$(\"#");
                js.append(inputRootId);
                js.append("\").attr(\"value\", node.id);");
                js.append("initTabel();");
            }



            js.append("});");
        }else{
            //tree 的 setting
            //tree初始化数据的引用名
            String initSettingName = "window."+this.getId()+"Setting";
            js.append(initSettingName);
            js.append(" = {");

            //异步
            js.append("async: { enable: true,},");

            String type = this.getType();
            if(!StringUtils.isEmpty(type)) {
                js.append("check: {enable: true,");
                if(StringUtils.equals(type,TYPE_CHECKBOX)) {
                    js.append("chkboxType: {\"Y\":\"\", \"N\":\"\"},");
//                js.append("chkboxType: \"all\",");
                }else{
                    js.append("chkStyle: \"radio\",");
                    js.append("radioType: \"all\"");
                }
                js.append("},");
            }
            js.append("view: {dblClickExpand: false},data: {simpleData: {enable: true}},");
            js.append("callback: {");

            if(!this.isParentCheck()) {
                js.append("beforeClick: beforeClick,");
            }

            if(!StringUtils.isEmpty(type)) {
                js.append("onCheck: onCheck,");
            }
            js.append("onClick: onClick,");
            js.append("}};");

            //添加function
            //beforeClick
            js.append("function beforeClick(treeId, treeNode) {var check = (treeNode && !treeNode.isParent);");
            js.append("if (!check) sk.failFaceMsg(\"");
            js.append(this.getParentNoCheckMsg());
            js.append("\");return check;}");

            //onCheck
            js.append("function onCheck(e, treeId, treeNode) {var zTree = $.fn.zTree.getZTreeObj(\"");
            js.append(this.getId());
            js.append("\"),");
            if(StringUtils.isEmpty(type)) {
                js.append("nodes = zTree.getSelectedNodes(),");
            }else{
                js.append("nodes = zTree.getCheckedNodes(true),");
            }
            js.append("rv=\"\";");
            js.append("nodes.sort(function compare(a,b){return a.id-b.id;});");
            js.append("for (var i=0, l=nodes.length; i<l; i++) {");
            js.append("rv += nodes[i].id + \",\";");
            js.append("}");
            js.append("if (rv.length > 0 ) rv = rv.substring(0, rv.length-1);");
            js.append("$(\"#");
            js.append(this.getInputValId());
            js.append("\").attr(\"value\", rv);");
            js.append("}");

            //onClick
            js.append("function onClick(e, treeId, treeNode) {var zTree = $.fn.zTree.getZTreeObj(\"");
            js.append(this.getId());
            js.append("\");");
            if(StringUtils.isEmpty(type)) {
                js.append("var nodes = zTree.getSelectedNodes(),");
                js.append("rv=\"\";");
                js.append("nodes.sort(function compare(a,b){return a.id-b.id;});");
                js.append("for (var i=0, l=nodes.length; i<l; i++) {");
                js.append("rv += nodes[i].id + \",\";");
                js.append("}");
                js.append("if (rv.length > 0 ) rv = rv.substring(0, rv.length-1);");
                js.append("$(\"#");
                js.append(this.getInputValId());
                js.append("\").attr(\"value\", rv);");
            }else{
                js.append("zTree.checkNode(treeNode, !treeNode.checked, null, true);");
                js.append("return false;");
            }

            js.append("}");


            //tree初始化
            js.append("$(document).ready(function(){");

            //tree初始化数据的引用名
            String initDataName = "window."+this.getId()+"InitData";

            //获取数据
            js.append(initDataName);
            js.append("=");
            if(!StringUtils.isEmpty(this.scCode)) {
                js.append(this.genData());
            }else{
                js.append("[]");
            }
            js.append(";");

            js.append("$.fn.zTree.init($(\"#");
            js.append(this.getId());
            js.append("\"), ");
            js.append(initSettingName);
            js.append(", ");

            js.append(initDataName);

            js.append(");");


            js.append("});");
        }

        js.append("</script>");
        return js.toString();
    }
    //生成数据
    private String genData(){
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
        List<Map<String,Object>> data = Lists.newArrayList();

        try {
            data = jdbcTemplate.queryForList(sql);
            if(!this.isInitTable) {
                //判断是否有值
                String value = this.getValue();
                if(!StringUtils.isEmpty(value)) {
                    String[] values = StringUtils.split(value,",");
                    int selectNum = 0;
                    for(int i = 0, len = data.size(); i < len; i++) {
                        Map<String,Object> item = data.get(i);
                        if(ArrayUtils.contains(values,item.get("id").toString())) {
                            //选中
                            data.get(i).put("checked",true);
                            selectNum++;
                            if(values.length == selectNum) {
                                break;
                            }
                        }
                    }
                }
            }

        }catch (DataAccessException e){
            log.error("sql语句配置错误:语句码为{}",sysSqlConfCustom.getScCode());
        }
        if(CollectionUtils.isEmpty(data)) {
            return null;
        }
        return JackJsonUtil.obj2String(data);
    }
}
