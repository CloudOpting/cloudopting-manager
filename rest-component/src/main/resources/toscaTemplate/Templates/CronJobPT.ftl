cron::job { '<#if name?has_content>${name}</#if>':
<#if minute?has_content>minute => '${minute}',</#if>
<#if hour?has_content>hour => '${hour}',</#if>
<#if date?has_content>date => '${date}',</#if>
<#if month?has_content>month => '${month}',</#if>
<#if weekday?has_content>weekday => '${weekday}',</#if>
<#if user?has_content>user => '${user}',</#if>
<#if command?has_content>command => '${command}',</#if>
<#if environment?has_content>environment => ${environment},</#if>
}