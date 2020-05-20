package cn.sk.mask.sys.custom.tag;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

@Setter
@Getter
@Slf4j
public class NavBarTag extends BaseTag {
    //标签名
    private static final String TAG_NAME  = "navBar";
    //html标签名
    private static final String HTML_TAG_NAME  = "nav";
    //是否是单标签
    private static final boolean IS_SINGLE_TAG  = Boolean.FALSE;
    //优先级
    private static final int PRECEDENCE = 10000;
    private static StringBuilder attrHtml = new StringBuilder();
    //特有的类样式
    private static String subsClazz = "breadcrumb";
    //导航栏的文字，多个用逗号分隔
    private String navText;
    //是否需要刷新按钮
    private boolean hasFresh;


    public NavBarTag(String dialectPrefix) {
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)

    }
    public NavBarTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        //要调用，不能去除
        super.doProcess(context, tag, structureHandler);
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

        //导航栏的文字，多个用逗号分隔
        String navText = tag.getAttributeValue("navText");
        this.setNavText(navText);

        //是否需要刷新按钮
        String hasFresh = tag.getAttributeValue("hasFresh");
        if(!StringUtils.isBlank(hasFresh)) {
            this.setHasFresh(Boolean.valueOf(hasFresh));
        }else{
            this.setHasFresh(Boolean.TRUE);
        }
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

            //业务代码开始
            html.append("<i class=\"Hui-iconfont\">&#xe67f;</i> 首页");
            String[] navTexts = StringUtils.split(this.getNavText(),",");
            for(int i = 0, len = navTexts.length; i < len; i++) {
                html.append(" <span class=\"c-gray en\">&gt;</span> ");
                html.append(navTexts[i]);
            }
            
            //判断是否要刷新按钮
            if(hasFresh) {
                html.append("<a class=\"btn btn-success radius r\" style=\"line-height:1.6em;margin-top:3px\"");
                html.append(" href=\"javascript:location.replace(location.href);\" title=\"刷新\">");
                html.append("<i class=\"Hui-iconfont\">&#xe68f;</i></a>");
            }
            //业务代码结束

            html.append("</");
            html.append(HTML_TAG_NAME);
            html.append(">");
        }

        return html.toString();
    }
}
