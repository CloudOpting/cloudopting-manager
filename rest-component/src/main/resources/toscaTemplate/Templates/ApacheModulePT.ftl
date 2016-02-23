class { 'apache::mod::<#if module?has_content>${module}</#if>':
      <#if settings?has_content>${settings}</#if>
}