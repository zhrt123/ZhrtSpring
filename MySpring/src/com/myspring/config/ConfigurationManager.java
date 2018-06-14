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

	// ��ȡ�����ļ����������bean����Ϣ
	public Map<String, Bean> getBeanconfig() {
		// ������Ϣ��name��Bean��ӳ��
		Map<String, Bean> beanMap = new HashMap<String, Bean>();
		try {
			// ������
			SAXBuilder builder = new SAXBuilder();
			// ���������ļ�
			InputStream is = ConfigurationManager.class.getResourceAsStream(path);
			Document doc = builder.build(is);
			// Document doc = builder.build(new File(path));
			Element root = doc.getRootElement();

			List<Element> beans = root.getChildren("bean");
			// ��������bean�ڵ㣬��������Ϣװ��beanMap��
			for (Element bean : beans) {
				Bean b = new Bean();
				// ����bean������
				b.setName(bean.getAttributeValue("name"));
				b.setClassName(bean.getAttributeValue("class"));
				String scope = bean.getAttributeValue("scope");
				// ���������scope��ʹ�����úõ�scope������ʹ��singleton
				// �н�׳�����⣺scope���ô���
				if (scope != null) {
					b.setScope(ScopeType.valueOf(scope));
				}
				// ����properties
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
							throw new Exception("������Ϣ����value��refֻ�ܴ���һ��");
						} else if (value != null) {
							p.setValue(value);
						} else {
							p.setRef(ref);
						}
						properties.add(p);
					}
				}
				b.setProperties(properties);
				// ��bean����bean����
				beanMap.put(b.getName(), b);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return beanMap;
	}

}
