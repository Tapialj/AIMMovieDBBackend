package com.aim.capstone.properties;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Component
@ConfigurationProperties(prefix = "cors")
public class CorsUrlProperties
{
  
  private String url;


  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

}
