mysql::db { '<#if dbname?has_content>${dbname}</#if>':
  user     => '<#if user?has_content>${user}</#if>',
  password => '<#if password?has_content>${password}</#if>',
  host     => '<#if host?has_content>${host}</#if>',
  grant    => ['SELECT', 'UPDATE'],
}
mysql::db { "<#if dbname?has_content>${dbname}</#if>_${fqdn}":
  user     => '<#if user?has_content>${user}</#if>',
  password => '<#if password?has_content>${password}</#if>',
  dbname   => '<#if dbname?has_content>${dbname}</#if>',
  host     => $::fqdn,
  grant    => ['SELECT', 'UPDATE'],
  tag      => $domain,
}

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>
