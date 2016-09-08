mysql::db { "<#if dbname?has_content>${dbname}</#if>":
  <#if user?has_content>user     => '${user}',</#if>
  <#if password?has_content>password => '${password}',</#if>
  <#if dbname?has_content>dbname   => '${dbname}',</#if>
  <#if host?has_content>host     => "${host}",</#if>
  <#if sql?has_content>sql     => "${sql}",</#if>
  <#if grant?has_content>grant     => "${grant}",</#if>
}
