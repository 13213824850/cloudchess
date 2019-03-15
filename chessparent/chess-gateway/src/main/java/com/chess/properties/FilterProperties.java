package com.chess.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author bystander
 * @date 2018/10/2
 */
@Data
@ConfigurationProperties(prefix = "chess.filter")
public class FilterProperties {

    private List<String> allowPaths;


}
