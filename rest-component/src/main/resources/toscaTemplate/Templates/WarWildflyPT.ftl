wildfly::deployment { '<#if name?has_content>${name}</#if>':
      <#if source?has_content>source    => '${source}',</#if>
      require => [ Exec['wait'], <#if require?has_content>${require},</#if> ],
}
