class { 'apache':
      <#if default_mods?has_content>default_mods => ${default_mods},</#if>
      <#if log_formats?has_content>log_formats => ${log_formats},</#if>
      <#if servername?has_content>servername => ${servername},</#if>
      <#if default_vhost?has_content>default_vhost => ${default_vhost},</#if>
      <#if mpm_module?has_content>mpm_module => ${mpm_module},</#if>
    }

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>
