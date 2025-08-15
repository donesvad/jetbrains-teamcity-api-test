<#import "common.ftl" as common>

<#macro change author='' amount=0 message='' link=''>
  <table align="center" width="100%" style="margin:8px 0 0" border="0" cellPadding="0" cellSpacing="0" role="presentation">
    <tbody>
    <tr>
      <td>
        <table align="center" width="100%" role="presentation" cellSpacing="0" cellPadding="0" border="0">
          <tbody style="width:100%">
          <tr style="width:100%">
            <p style="font-size:12px;line-height:24px;margin:0;color:#737577">By ${author} (<a href="${link}" target="_blank" style="color:#0F5B99;text-decoration:none">${amount} file<@common.plural val=amount /></a>)</p>
          </tr>
          </tbody>
        </table>
        <table align="center" width="100%" role="presentation" cellSpacing="0" cellPadding="0" border="0">
          <tbody style="width:100%">
          <tr style="width:100%">
            <p style="font-size:14px;line-height:24px;margin:0;color:#1F2326">${message}</p>
          </tr>
          </tbody>
        </table>
      </td>
    </tr>
    </tbody>
  </table>
</#macro>

<#macro changes bean link>
    <#-- @ftlvariable name="bean" type="jetbrains.buildServer.notification.impl.ChangesBean" -->
    <#-- @ftlvariable name="webLinks" type="jetbrains.buildServer.serverSide.WebLinks" -->

    <#assign modNum=bean.modificationsNumber/>
    <#if (modNum > 0)>
    <table align="center" width="100%" id="layout-container" role="presentation" cellSpacing="0" cellPadding="0" border="0" style="max-width:650px;padding:0">
      <tbody>
      <tr style="width:100%">
        <td>
          <table align="center" width="100%" border="0" cellPadding="0" cellSpacing="0" role="presentation">
            <tbody>
            <tr>
              <td>
                <table align="center" width="100%" role="presentation" cellSpacing="0" cellPadding="0" border="0">
                  <tbody style="width:100%">
                  <tr style="width:100%">
                    <p style="font-size:14px;line-height:24px;margin:32px 0 8px">${modNum} change<@common.plural modNum /></p>
                  </tr>
                  </tbody>
                </table>
                <#list bean.modifications as mod>
                  <#assign description=mod.description?html/>
                  <@change
                    author=mod.userName
                    amount=mod.changeCount
                    message=mod.description?replace("(\r?\n|\r)", "<br>", "r")
                    link=link
                  />
                </#list>
              </td>
            </tr>
            </tbody>
          </table>
        </td>
      </tr>
      </tbody>
    </table>
  </#if>
</#macro>
