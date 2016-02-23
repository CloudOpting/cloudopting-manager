apache::vhost { '<#if servername?has_content>${servername}</#if>':
      <#if port?has_content>port    => ${port},</#if>
      <#if vhost_name?has_content>vhost_name    => '${vhost_name}',</#if>
      <#if servername?has_content>servername    => '${servername}',</#if>
      <#if docroot?has_content>docroot => '${docroot}',</#if>
      <#if aliases?has_content>aliases => ${aliases},</#if>
      <#if directories?has_content>directories => ${directories},</#if>
      <#if log_level?has_content>log_level => ${log_level},</#if>
      <#if options?has_content>options => ${options},</#if>
      <#if proxy_pass?has_content>proxy_pass => ${proxy_pass},</#if>
      <#if proxy_preserve_host?has_content>proxy_preserve_host => ${proxy_preserve_host},</#if>
      <#if redirect_source?has_content>redirect_source => ${redirect_source},</#if>
      <#if redirect_dest?has_content>redirect_dest => ${redirect_dest},</#if>
      <#if redirect_status?has_content>redirect_status => ${redirect_status},</#if>
      <#if rewrites?has_content>rewrites => ${rewrites},</#if>
}