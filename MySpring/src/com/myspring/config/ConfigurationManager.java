package com.myspring.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.myspring.config.Bean.ScopeType;

public class ConfigurationManager {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ConfigurationManager(String path) {
		setPath(path);
	}

	// 读取配置文件，获得所有bean的信息
	public Map<String, Bean> getBeanconfig() {
		// 返回信息，name与Bean的映射
		Map<String, Bean> beanMap = new HashMap<String, Bean>();
		try {
			// 解析器
			SAXBuilder builder = new SAXBuilder();
			// 加载配置文件
			InputStream is = ConfigurationManager.class.getResourceAsStream(path);
			Document doc = builder.build(is);
			// Document doc = builder.build(new File(path));
			Element root = doc.getRootElement();

			List<Element> beans = root.getChildren("bean");
			// 遍历所有bean节点，将配置信息装入beanMap中
			for (Element bean : beans) {
				Bean b = new Bean();
				// 配置bean的属性
				b.setName(bean.getAttributeValue("name"));
				b.setClassName(bean.getAttributeValue("class"));
				String scope = bean.getAttributeValue("scope");
				// 如果配置了scope则使用配置好的scope，否则使用singleton
				// 有健壮性问题：scope配置错误
				if (scope != null) {
					b.setScope(ScopeType.valueOf(scope));
				}
				// 配置properties
				List<Property> properties = new ArrayList<Property>();
				List<Element> propertyNodes = bean.getChildren("property");
				if (propertyNodes != null) {
					for (Element property : propertyNodes) {
						String name = property.getAttributeValue("name");
						String value = property.getAttributeValue("value");
						String ref = property.getAttributeValue("ref");
						if (property.getAttributeValue("name").equals("interceptorName")) {
							List<Element> interceptorNames = property.getChild("list").getChildren("value");
							for (Element interceptorName : interceptorNames) {
								Property p = new Property();
								p.setName("interceptorName");
								p.setRef(interceptorName.getText());
								properties.add(p);
							}
							continue;
						}
						Property p = new Property();
						p.setName(name);
						if ((value == null && ref == null) || (value != null && ref != null)) {
							throw new Exception("配置信息错误，value和ref只能存在一个");
						} else if (value != null) {
							p.setValue(value);
						} else {
							p.setRef(ref);
						}
						properties.add(p);
					}
				}
				b.setProperties(properties);
				// 将bean放入bean池中
				beanMap.put(b.getName(), b);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return beanMap;
	}

}
