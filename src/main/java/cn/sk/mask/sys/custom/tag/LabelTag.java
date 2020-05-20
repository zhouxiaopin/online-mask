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
public class LabelTag extends BaseTag {
    //标签名
    private static final String TAG_NAME  = "label";
    //html标签名
    private static final String HTML_TAG_NAME  = "label";
    //是否是单标签
    private static final boolean IS_SINGLE_TAG  = Boolean.FALSE;
    //优先级
    private static final int PRECEDENCE = 10000;
    private static StringBuilder attrHtml = new StringBuilder();
    //特有的类样式
    private static String subsClazz = "form-label";
    //是否是必须填写
    private boolean required;
    //显示文本
    private String text;


    public LabelTag(String dialectPrefix) {
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)

    }
    public LabelTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
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

            if(this.isRequired()) {
                html.append("<span class=\"c-red\">*</span>");
            }
            html.append(this.getText());

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

        String required = tag.getAttributeValue("required");
        if(!StringUtils.isEmpty(required)) {
            this.setRequired(Boolean.valueOf(required));
        }else{
            this.setRequired(Boolean.FALSE);
        }

        String text = tag.getAttributeValue("text");
        if(!StringUtils.isEmpty(text)) {
            this.setText(text);
        }

    }

    @Override
    protected String getSubsClazz() {
        return subsClazz;
    }
}
