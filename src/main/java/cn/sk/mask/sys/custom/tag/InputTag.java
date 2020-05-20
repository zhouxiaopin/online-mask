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
public class InputTag extends BaseTag {
    //标签名
    private static final String TAG_NAME  = "input";
    //html标签名
    private static final String HTML_TAG_NAME  = "input";
    //是否是单标签
    private static final boolean IS_SINGLE_TAG  = Boolean.TRUE;
    //优先级
    private static final int PRECEDENCE = 10000;
    private static StringBuilder attrHtml = new StringBuilder();
    //特有的类样式
    private static String subsClazz = "input-text";

    //类型
    private String type;
    //hint提示
    private String placeholder;
    //值
    private String value;
    //是否只读
    private boolean readonly = Boolean.FALSE;


    public InputTag(String dialectPrefix) {
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)

    }
    public InputTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
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

            html.append("</");
            html.append(HTML_TAG_NAME);
            html.append(">");
        }
        return html.toString();
    }

    @Override
    protected void setAttributeValue(IProcessableElementTag tag) {
        //要调用，不能去除
        super.setAttributeValue(tag);
        attrHtml.delete(0,attrHtml.length());
        //类型
        String type = tag.getAttributeValue("type");
        if(!StringUtils.isEmpty(type)) {
            this.setType(type);
        }else{
            this.setType("text");
        }
        attrHtml.append(" type=\"");
        attrHtml.append(this.getType());
        attrHtml.append("\"");

        //hint提示
        String placeholder = tag.getAttributeValue("placeholder");
        if(!StringUtils.isEmpty(placeholder)) {
            this.setPlaceholder(placeholder);
            attrHtml.append(" placeholder=\"");
            attrHtml.append(placeholder);
            attrHtml.append("\"");
        }

        //值
        String value = tag.getAttributeValue("value");
        if(!StringUtils.isEmpty(value)) {
            this.setValue(value);
            attrHtml.append(" value=\"");
            attrHtml.append(value);
            attrHtml.append("\"");
        }

        //是否只读
        String readonly = tag.getAttributeValue("readonly");
        if(!StringUtils.isEmpty(readonly)) {
            this.setReadonly(Boolean.valueOf(readonly));
            attrHtml.append(" readonly");
        }

    }

    @Override
    protected String getSubsClazz() {
        return subsClazz;
    }
}
