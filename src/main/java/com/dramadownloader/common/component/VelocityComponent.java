package com.dramadownloader.common.component;

import com.dramadownloader.common.template.VelocityTemplateEngine;

public class VelocityComponent {
  private final VelocityTemplateEngine _velocityTemplateEngine;

  public VelocityComponent() {
    _velocityTemplateEngine = new VelocityTemplateEngine();
  }

  public VelocityTemplateEngine getVelocityTemplateEngine() {
    return _velocityTemplateEngine;
  }
}
