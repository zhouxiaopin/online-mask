package cn.sk.mask.sys.custom.tag;

import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.custom.service.ITreeSelectService;
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
public class TreeSelectTag extends BaseTag {
    //标签名
    private static final String TAG_NAME  = "treeSelect";
    //html标签名
//    private static final String HTML_TAG_NAME  = "select";
    //是否是单标签
//    private static final boolean IS_SINGLE_TAG  = Boolean.FALSE;
    //优先级
    private static final int PRECEDENCE = 10000;
    private static StringBuilder attrHtml = new StringBuilder();
    private static final String TYPE_CHECKBOX = "checkbox";
    private static final String TYPE_RADIO = "radio";
    //特有的类样式
    private static String subsClazz = "";
    //类型 checkbox,radio
    private String type;
    //父节点是否可选
    private boolean parentCheck = Boolean.TRUE;
    //父节点不可选提示信息
    private String parentNoCheckMsg = "父节点不能选择";
    //语句编码
    private String scCode;

    //树形选择后显示的元素id
    private String inputShowId = "_sk_"+System.nanoTime();
    //树形选择后表单提交的元素id
    private String inputValId = "_sk_"+System.nanoTime();
    //树形下拉框外面元素div的id
    private String divId = "_sk_"+System.nanoTime();
    //value对应显示的值
    private String showValue;

    //是否使用外部数据
    private boolean useOutData = Boolean.FALSE;
    //ITreeSelectService执行的方法
    private String exeMethod;

//    {id:5, pId:0, name:"广东省", open:true}
//    {id:4, pId:0, name:"河北省", open:true, nocheck:true},
    private ISysSqlConfService sysSqlConfService;
    private JdbcTemplate jdbcTemplate;
    private ITreeSelectService treeSelectService;

    public TreeSelectTag(String dialectPrefix) {
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)

    }
    public TreeSelectTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        //要调用，不能去除
        super.doProcess(context, tag, structureHandler);
        sysSqlConfService = appCtx.getBean(ISysSqlConfService.class);
        jdbcTemplate = appCtx.getBean(JdbcTemplate.class);
        if(useOutData) {
            treeSelectService = appCtx.getBean(ITreeSelectService.class);
        }

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

        //语句编码
        String scCode = tag.getAttributeValue("scCode");
        if(!StringUtils.isEmpty(scCode)) {
            this.setScCode(scCode);
        }
        //是否使用外部数据
        String useOutData = tag.getAttributeValue("useOutData");
        if(!StringUtils.isEmpty(useOutData)) {
            this.setUseOutData(Boolean.valueOf(useOutData));
        }
        //ITreeSelectService执行的方法
        String exeMethod = tag.getAttributeValue("exeMethod");
        if(!StringUtils.isEmpty(exeMethod)) {
            this.setExeMethod(exeMethod);
        }

    }

    @Override
    protected String getSubsClazz() {
        return subsClazz;
    }

    //初始化属性值
    private void initAttributeValue(){
        //特有的类样式
//        subsClazz = "";
        //类型 checkbox,radio
        this.setType("");
        //父节点是否可选
        this.setParentCheck(Boolean.TRUE);
        //父节点不可选提示信息
        this.setParentNoCheckMsg("父节点不能选择");
        //语句编码
        this.setScCode("");
        //value对应显示的值
        this.setShowValue("");

        //是否使用外部数据
        this.setUseOutData(Boolean.FALSE);
        //ITreeSelectService执行的方法
        this.setExeMethod("");
    }

    //生成html
    private String genHtml(){
        StringBuilder html = new StringBuilder();

        //判断id是否为空
        if(StringUtils.isEmpty(this.getId())) {
            this.setId("_sk_"+System.nanoTime());
        }

        html.append("<input readonly onclick=\"showMenu(); return false;\" id=\"");
        html.append(this.getInputShowId());
        html.append("\" placeholder=\"");
        html.append("----请选择----");
        html.append("\" class=\"input-text\" />");

        //表单提交的值
        html.append("<input id=\"");
        html.append(this.getInputValId());
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

        html.append("<div id=\"");
        html.append(this.getDivId());
        html.append("\" class=\"skTree\">");
        html.append("<ul id=\"");
        html.append(this.getId());
        html.append("\" class=\"ztree\"></ul>");
        html.append("</div>");

        //获取js
        html.append(this.genJs());
//        System.out.println(this.genJs());
        return html.toString();
    }
    //生成js
    private String genJs() {
        StringBuilder js = new StringBuilder();
        js.append("<script type=\"text/javascript\">");

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
        js.append("function onCheck(e, treeId, treeNode) {");
        if(!this.isParentCheck()) {
            js.append("var check = (treeNode && !treeNode.isParent);");
            js.append("if (!check){ sk.failFaceMsg(\"");
            js.append(this.getParentNoCheckMsg());
            js.append("\");return check;}");
        }
        js.append("var zTree = $.fn.zTree.getZTreeObj(\"");
        js.append(this.getId());
        js.append("\"),");
        if(StringUtils.isEmpty(type)) {
            js.append("nodes = zTree.getSelectedNodes(),");
        }else{
            js.append("nodes = zTree.getCheckedNodes(true),");
        }
        js.append("v = \"\",rv=\"\";");
        js.append("nodes.sort(function compare(a,b){return a.id-b.id;});");
        js.append("for (var i=0, l=nodes.length; i<l; i++) {");
        js.append("v += nodes[i].name + \",\";");
        js.append("rv += nodes[i].id + \",\";");
        js.append("}");
        js.append("if (v.length > 0 ) v = v.substring(0, v.length-1);");
        js.append("if (rv.length > 0 ) rv = rv.substring(0, rv.length-1);");
        js.append("$(\"#");
        js.append(this.getInputShowId());
        js.append("\").attr(\"value\", v);");
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
            js.append("v = \"\",rv=\"\";");
            js.append("nodes.sort(function compare(a,b){return a.id-b.id;});");
            js.append("for (var i=0, l=nodes.length; i<l; i++) {");
            js.append("v += nodes[i].name + \",\";");
            js.append("rv += nodes[i].id + \",\";");
            js.append("}");
            js.append("if (v.length > 0 ) v = v.substring(0, v.length-1);");
            js.append("if (rv.length > 0 ) rv = rv.substring(0, rv.length-1);");
            js.append("$(\"#");
            js.append(this.getInputShowId());
            js.append("\").attr(\"value\", v);");
            js.append("$(\"#");
            js.append(this.getInputValId());
            js.append("\").attr(\"value\", rv);");
        }else{
            js.append("zTree.checkNode(treeNode, !treeNode.checked, null, true);");
            js.append("return false;");
        }

        js.append("}");

        //showMenu
        js.append("function showMenu() {");
        js.append("var $inputShow = $(\"#");
        js.append(this.getInputShowId());
        js.append("\");");
        js.append("var width = $inputShow.width();");
        js.append("$(\"#");
        js.append(this.getDivId());
        js.append("\").css({ top:$inputShow.outerHeight() + \"px\",width:width+'px'}).slideDown(\"fast\");");
        js.append("$(\"body\").bind(\"mousedown\", onBodyDown);");
        js.append("}");

        //hideMenu
        js.append("function hideMenu() {");
        js.append("$(\"#");
        js.append(this.getDivId());
        js.append("\").fadeOut(\"fast\");");
        js.append("$(\"body\").unbind(\"mousedown\", onBodyDown);};");

        //onBodyDown
        js.append("function onBodyDown(event) {");
        js.append("if (!(event.target.id == \" ");
        js.append(this.getInputShowId());
        js.append("\" || event.target.id == \"");
        js.append(this.getDivId());
        js.append("\" || ");
        js.append(" $(event.target).parents(\"#");
        js.append(this.getDivId());
        js.append("\").length>0)) {");
        js.append("hideMenu();");
        js.append("}}");

        //tree初始化
        js.append("$(document).ready(function(){");

        //tree初始化数据的引用名
        String initDataName = "window."+this.getId()+"InitData";

        //获取数据
        js.append(initDataName);
        js.append("=");
        if(!StringUtils.isEmpty(this.scCode)||!StringUtils.isEmpty(this.exeMethod)) {
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

        //判断否有显示的值
        String sv = this.getShowValue();
        if(!StringUtils.isEmpty(sv)) {
            js.append("$(\"#");
            js.append(this.getInputShowId());
            js.append("\").attr(\"value\",\"");
            js.append(sv);
            js.append("\");");
        }


        js.append("});");
        js.append("</script>");
        return js.toString();
    }
    //生成数据
    private String genData(){
        String sql = "";
        if(!useOutData) {
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

            sql = sysSqlConfCustoms.get(0).getScStatement();
        }


        List<Map<String,Object>> data = Lists.newArrayList();

        try {
            if(!useOutData){
                data = jdbcTemplate.queryForList(sql);
            }else {
                data = treeSelectService.exeMethod(exeMethod).getData();
            }

            //判断数据是否为空
            if(CollectionUtils.isEmpty(data)) {
                return "[]";
            }

            //判断是否有值

            String value = this.getValue();
            if(!StringUtils.isEmpty(value)) {
                String[] values = StringUtils.split(value,",");
                StringBuilder temp = new StringBuilder();
                int selectNum = 0;
                for(int i = 0, len = data.size(); i < len; i++) {
                    Map<String,Object> item = data.get(i);
                    if(ArrayUtils.contains(values,item.get("id").toString())) {
                        //选中
                        data.get(i).put("checked",true);
                        temp.append(item.get("name").toString()).append(",");
                        selectNum++;
                        if(values.length == selectNum) {
                            break;
                        }
                    }
                }
                if(temp.length()>0) {
                    temp.deleteCharAt(temp.length()-1);//取出最后一个逗号
                }
                this.setShowValue(temp.toString());
            }
        }catch (DataAccessException e){
//            log.error("sql语句配置错误:语句码为{}",sysSqlConfCustom.getScCode());
            log.error("sql语句配置错误:语句码为{}",this.getScCode());
        }
        if(CollectionUtils.isEmpty(data)) {
            return null;
        }
        return JackJsonUtil.obj2String(data);
    }
}
