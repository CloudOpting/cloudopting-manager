mongodb_database { '<#if mongodb_dbname?has_content>${mongodb_dbname}</#if>':
      <#if mongodb_user?has_content>user    => '${mongodb_user}',</#if>
      <#if mongodb_password?has_content>password => '${mongodb_password}',</#if>
      require => Class['mongodb::server']
}

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>
