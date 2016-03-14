package{"wget":
}->
exec {<#if source?has_content>'${source}':</#if>
command => "wget --verbose --user=<#if co_jack_user?has_content>${co_jack_user}</#if> --password=<#if co_jack_password?has_content>${co_jack_password}</#if> --output-document=\"<#if destination?has_content>${destination}</#if>/<#if archive?has_content>${archive}</#if>.<#if extension?has_content>${extension}</#if>\" \"<#if source?has_content>${source}</#if>\"",
path => '/bin:/usr/bin:/usr/sbin',
<#if require?has_content>require => ${require},</#if>
<#if before?has_content>before => ${before},</#if>
}<#if (unzip?has_content) && unzip == "true">->
package{"unzip":
}->
archive::extract{<#if archive?has_content>'${archive}':</#if>
<#if target?has_content>target => '${target}',</#if>
<#if destination?has_content>src_target => '${destination}',</#if>
<#if extension?has_content>extension => '${extension}',</#if>
<#if archive_user?has_content>user => '${archive_user}',</#if>
<#if root_dir?has_content>root_dir => ${root_dir},</#if>
}
</#if>
