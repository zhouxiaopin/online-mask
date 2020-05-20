package cn.sk.mask.sys.custom.tag;

import cn.sk.mask.sys.common.ServerResponse;
import cn.sk.mask.sys.common.SysConst;
import cn.sk.mask.sys.pojo.SysDictCustom;
import cn.sk.mask.sys.pojo.SysDictQueryVo;
import cn.sk.mask.sys.service.ISysDictService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

@Setter
@Getter
@Slf4j
public class AbtnTag extends BaseTag {
    //标签名
    private static final String TAG_NAME  = "aBtn";
    //html标签名
    private static final String HTML_TAG_NAME  = "a";
    //是否是单标签
    private static final boolean IS_SINGLE_TAG  = Boolean.FALSE;
    //优先级
    private static final int PRECEDENCE = 10000;
    //标签属性
    private static final StringBuilder attrHtml = new StringBuilder();
    //特有的类样式
    private static final String subsClazz = "btn radius";


    //字典类型
    private String dictType;
    //字典编码
    private String dictCode;
    //按钮显示的文字
    private String text;

    private ISysDictService sysDictService;


    public AbtnTag(String dialectPrefix) {
        super(
                TemplateMode.HTML, // 此处理器将仅应用于HTML模式
                dialectPrefix,     // 要应用于名称的匹配前缀
                TAG_NAME,          // 标签名称：匹配此名称的特定标签
                true,              // 将标签前缀应用于标签名称
                null,              // 无属性名称：将通过标签名称匹配
                false,             // 没有要应用于属性名称的前缀
                PRECEDENCE);       // 优先(内部方言自己的优先)

    }
    public AbtnTag(TemplateMode templateMode, String dialectPrefix, String elementName, boolean prefixElementName, String attributeName, boolean prefixAttributeName, int precedence) {
        super(templateMode, dialectPrefix, elementName, prefixElementName, attributeName, prefixAttributeName, precedence);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        //要调用，不能去除
        super.doProcess(context, tag, structureHandler);

        sysDictService = appCtx.getBean(ISysDictService.class);

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

        //字典类型
        String dictType = tag.getAttributeValue("dictType");
        this.setDictType(dictType);
        //字典编码
        String dictCode = tag.getAttributeValue("dictCode");
        this.setDictCode(dictCode);

        //按钮显示的文字
        String text = tag.getAttributeValue("text");
        this.setText(text);

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
            if(!StringUtils.isEmpty(this.dictType)&&!StringUtils.isEmpty(this.dictCode)) {
                html.append(this.genBtnIcon());
            }

            html.append(this.text);
            //业务代码结束

            html.append("</");
            html.append(HTML_TAG_NAME);
            html.append(">");
        }

        return html.toString();
    }

    //生成按钮图标
    private String genBtnIcon(){
        StringBuilder html = new StringBuilder();

        //根据数据生成
        SysDictQueryVo sysDictQueryVo = new SysDictQueryVo();

        SysDictCustom condition = new SysDictCustom();
        condition.setDictType(this.dictType);
        condition.setDictCode(this.dictCode);
        condition.setRecordStatus(SysConst.RecordStatus.ABLE);

        sysDictQueryVo.getIsNoLike().put("dictType",true);
        sysDictQueryVo.setSysDictCustom(condition);

        ServerResponse<List<SysDictCustom>> sr = sysDictService.queryObjs(sysDictQueryVo);
        List<SysDictCustom> data = sr.getData();
        for (int i = 0,len = data.size(); i < len; i++){
            SysDictCustom s = data.get(i);
            html.append(s.getCodeName());
            html.append(" ");
        }

        return html.toString();
    }
}
