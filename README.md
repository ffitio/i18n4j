# i18n4j
![Maven](https://img.shields.io/maven-central/v/io.ffit.carbon/i18n4j-core.svg)
![License](https://img.shields.io/github/license/ffitio/i18n4j.svg)

### Overview

a Java internationalization components

### Installation(Maven)

Add the dependency to your pom.xml file:

```
<dependency>
    <groupId>io.ffit.carbon</groupId>
    <artifactId>i18n4j-core</artifactId>
    <version>1.1.0</version>
</dependency>

<dependency>
    <groupId>io.ffit.carbon</groupId>
    <artifactId>i18n4j-data-local</artifactId>
    <version>1.1.0</version>
</dependency>
```

Then run from the root dir of the project:

```
mvn install
```

### Example

#### resources file structure
```
resources  
    |--i18n  
       |--index.yaml  
       |--global  
          |--global_en.yaml  
          |--global_zh_CN.yaml  
```

The `index.yaml` file content:
```yaml
global:
  - en
  - zh-CN
```

The `global_en.yaml` file content:
```yaml
http:
    internalServerError: "Internal Server Error"
```

The `global_zh_CN.yaml` file content:
```yaml
http:
  internalServerError: "服务器内部错误"
```

`Example.java`

```java
import io.ffit.carbon.i18n4j.core.DefaultI18nSource;
import io.ffit.carbon.i18n4j.core.I18nSource;

import java.util.Locale;

class Application {
    public static void main(String[] args) {
        I18nSource i18nSource = new DefaultI18nSource(LocalI18nConfig.DEFAULT);
        String code = "http.internalServerError";
        System.out.printf("Message of English: %s \n", i18nSource.getMessage(code, Locale.ENGLISH));
        System.out.printf("Message of Simplified Chinese: %s \n", i18nSource.getMessage(code, Locale.SIMPLIFIED_CHINESE));
    }
}
```