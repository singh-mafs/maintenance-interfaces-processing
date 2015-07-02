package com.mikealbert.processing.routes;

import org.apache.camel.builder.RouteBuilder;

public class BridgestoneFirestoneInputRoute extends RouteBuilder {
  
  @Override
  public void configure() throws Exception {
    from("{{bridgestoneFirestone.sftp.url}}")
    .to("file:{{files.input}}\\Bridgestone_Firestone");
  }
}
