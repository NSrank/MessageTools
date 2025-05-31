# PowerShell脚本：修复velocity-plugin.json文件
# 使用方法：在项目根目录运行 powershell -ExecutionPolicy Bypass -File fix-plugin-json.ps1

Write-Host "修复 MessageTools 插件的 velocity-plugin.json 文件..." -ForegroundColor Green

# 正确的JSON内容
$correctJson = @'
{
  "id": "messagetools",
  "name": "MessageTools",
  "version": "1.0-SNAPSHOT",
  "description": "A Velocity plugin for customizable join/quit messages with PlaceholderAPI support",
  "authors": ["Plugin Developer"],
  "main": "org.plugin.messagetools.MessageTools"
}
'@

# 检查并创建target/classes目录
$targetDir = "target/classes"
if (!(Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir -Force
    Write-Host "创建目录: $targetDir" -ForegroundColor Yellow
}

# 写入正确的JSON文件
$jsonPath = "$targetDir/velocity-plugin.json"
$correctJson | Out-File -FilePath $jsonPath -Encoding UTF8
Write-Host "已修复: $jsonPath" -ForegroundColor Green

# 重新打包JAR文件
Write-Host "重新打包JAR文件..." -ForegroundColor Yellow
try {
    & mvn jar:jar -q
    Write-Host "JAR文件打包完成!" -ForegroundColor Green
} catch {
    Write-Host "打包失败，请手动运行: mvn jar:jar" -ForegroundColor Red
}

# 验证JAR文件
$jarPath = "target/MessageTools-1.0-SNAPSHOT.jar"
if (Test-Path $jarPath) {
    Write-Host "插件JAR文件已生成: $jarPath" -ForegroundColor Green
    Write-Host "文件大小: $((Get-Item $jarPath).Length) 字节" -ForegroundColor Cyan
} else {
    Write-Host "警告: JAR文件未找到!" -ForegroundColor Red
}

Write-Host "`n修复完成! 现在可以将JAR文件部署到Velocity服务器了。" -ForegroundColor Green
Write-Host "部署路径: plugins/MessageTools-1.0-SNAPSHOT.jar" -ForegroundColor Cyan
