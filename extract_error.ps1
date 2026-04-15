Get-Content debug_log.txt | Select-String -Pattern "e: file" -Context 0,5
