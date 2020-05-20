package cn.sk.mask.sys.custom.tag;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

@Setter
@Getter
public abstract class BaseTag extends AbstractElementTagProcessor {

    private String id;//标签id
    private String clazz;//类样式
    private String name;//名字
    private String onclick;//点击事件
    private String style;//样式
    private String value;//值
    private String onfocus;//当获取焦点
    private String disabled;//是否可用

    //获取应用程序上下文
    protected static ApplicationContext appCtx;
    private static StringBuilder baseAttrHtml = new StringBuilder();

    public BaseTag() {
        super(null, null, null, true, null, true, 1000);
    }

    public BaseTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        appCtx = SpringContextUtils.getApplicationContext(context);
        setAttributeValue(tag);
    }

    //设置属性值
    protected void setAttributeValue(IProcessableElementTag tag){
        baseAttrHtml.delete(0,baseAttrHtml.length());

        String id = tag.getAttributeValue("id");
        this.setId("");
        if(!StringUtils.isEmpty(id)) {
            this.setId(id);
            baseAttrHtml.append(" id=\"");
            baseAttrHtml.append(id);
            baseAttrHtml.append("\"");
        }

        String clazz = tag.getAttributeValue("class");
        this.setClazz("");
        if(!StringUtils.isEmpty(clazz)) {
            this.setClazz(clazz);
            baseAttrHtml.append(" class=\"");
            String subsClazz = this.getSubsClazz();
            if(!StringUtils.isEmpty(subsClazz)) {
                baseAttrHtml.append(subsClazz);
                baseAttrHtml.append(" ");
            }
            baseAttrHtml.append(clazz);
            baseAttrHtml.append("\"");
        }else {
            String subsClazz = this.getSubsClazz();
            if(!StringUtils.isEmpty(subsClazz)) {
                this.setClazz(subsClazz);
                baseAttrHtml.append(" class=\"");
                baseAttrHtml.append(subsClazz);
                baseAttrHtml.append("\"");
            }
        }

        String name = tag.getAttributeValue("name");
        this.setName("");
        if(!StringUtils.isEmpty(name)) {
            this.setName(name);
            baseAttrHtml.append(" name=\"");
            baseAttrHtml.append(name);
            baseAttrHtml.append("\"");
        }

        String onclick = tag.getAttributeValue("onclick");
        this.setOnclick("");
        if(!StringUtils.isEmpty(onclick)) {
            this.setOnclick(onclick);
            baseAttrHtml.append(" onclick=\"");
            baseAttrHtml.append(onclick);
            baseAttrHtml.append("\"");
        }
        //样式
        String style = tag.getAttributeValue("style");
        this.setStyle("");
        if(!StringUtils.isEmpty(style)) {
            this.setStyle(style);
            baseAttrHtml.append(" style=\"");
            baseAttrHtml.append(style);
            baseAttrHtml.append("\"");
        }
        //值
        String value = tag.getAttributeValue("value");
        this.setValue("");
        if(!StringUtils.isEmpty(value)) {
            this.setValue(value);
            baseAttrHtml.append(" value=\"");
            baseAttrHtml.append(value);
            baseAttrHtml.append("\"");
        }
        //当获取焦点事件
        String onfocus = tag.getAttributeValue("onfocus");
        this.setOnfocus(onfocus);
        if(!StringUtils.isEmpty(onfocus)) {
            baseAttrHtml.append(" onfocus=\"");
            baseAttrHtml.append(onfocus);
            baseAttrHtml.append("\"");
        }

        //是否可用
        String disabled = tag.getAttributeValue("disabled");
        this.setDisabled(disabled);
        if(!StringUtils.isEmpty(disabled)) {
            baseAttrHtml.append(" disabled=\"");
            baseAttrHtml.append(disabled);
            baseAttrHtml.append("\"");
        }
    }

    //获取子类的类样式
    protected abstract String getSubsClazz();

    //获取基本的属性
    protected String getBaseAttrHtml(){
        return baseAttrHtml.toString();
    }


}
