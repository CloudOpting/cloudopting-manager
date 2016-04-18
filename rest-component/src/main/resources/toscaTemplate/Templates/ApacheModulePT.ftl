class { 'apache::mod::<#if module?has_content>${module}</#if>':
      <#if settings?has_content>${settings}</#if>
      <#if require?has_content>require => ${require},</#if> 
      <#if before?has_content>before => ${before},</#if>
}