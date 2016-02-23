tomcat::war { '<#if name?has_content>${name}</#if>':
      <#if catalina_base?has_content>catalina_base    => '${catalina_base}',</#if>
      <#if war_source?has_content>war_source => ${war_source},</#if>
}