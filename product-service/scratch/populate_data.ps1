# populate_data.ps1
# Script to populate the product-service with dummy categories and products including images.

$baseUrl = "http://localhost:8086/api"
$scratchDir = "e:/Personal/microservices-learning/ecommerce-microservices-learning/product-service/scratch"
$imagePath = Join-Path $scratchDir "dummy.jpg"

# 1. Create a dummy image if it doesn't exist
if (-not (Test-Path $imagePath)) {
    Write-Host "Creating dummy image..."
    Add-Type -AssemblyName System.Drawing
    $img = New-Object System.Drawing.Bitmap 100, 100
    $img.Save($imagePath, [System.Drawing.Imaging.ImageFormat]::Jpeg)
    $img.Dispose()
}

Write-Host "Starting Data Population..."

# 2. Create Categories
$categories = @(
    @{ title = "Electronics" },
    @{ title = "Fashion" },
    @{ title = "Home & Garden" },
    @{ title = "Sports" },
    @{ title = "Beauty" }
)

$categoryIds = @{}

foreach ($cat in $categories) {
    Write-Host "Creating Category: $($cat.title)..."
    
    $json = @{
        categoryTitle = $cat.title
    } | ConvertTo-Json

    $tempFile = Join-Path $scratchDir "temp_cat.json"
    $json | Out-File -FilePath $tempFile -Encoding utf8

    # Use curl with more verbose output for debugging if needed
    $response = curl.exe -s -X POST "$baseUrl/categories" `
        -H "Accept: application/json" `
        -F "dto=@$tempFile;type=application/json" `
        -F "files=@$imagePath"

    Write-Host "Response: $response"
    
    $resObj = $response | ConvertFrom-Json
    $categoryIds[$cat.title] = $resObj.id
    
    Remove-Item $tempFile
    Write-Host "Created Category [$($cat.title)] with ID: $($resObj.id)"
}

# 3. Create Products
$products = @(
    @{ name = "Smartphone X1"; price = 799.99; category = "Electronics" },
    @{ name = "Wireless Headphones"; price = 199.99; category = "Electronics" },
    @{ name = "Cotton T-Shirt"; price = 25.00; category = "Fashion" },
    @{ name = "Leather Jacket"; price = 150.00; category = "Fashion" },
    @{ name = "Garden Rake"; price = 30.00; category = "Home & Garden" },
    @{ name = "Modern Sofa"; price = 899.00; category = "Home & Garden" },
    @{ name = "Yoga Mat"; price = 45.00; category = "Sports" },
    @{ name = "Running Shoes"; price = 120.00; category = "Sports" },
    @{ name = "Face Serum"; price = 35.00; category = "Beauty" },
    @{ name = "Lipstick Set"; price = 60.00; category = "Beauty" }
)

foreach ($prod in $products) {
    Write-Host "Creating Product: $($prod.name)..."
    
    $json = @{
        name = $prod.name
        price = $prod.price
        categoryId = $categoryIds[$prod.category]
        status = "ACTIVE"
        stockStatus = "IN_STOCK"
        stockQuantity = 100
        description = "This is a high quality $($prod.name)"
        sku = "SKU-$(Get-Random -Minimum 1000 -Maximum 9999)"
    } | ConvertTo-Json

    $tempFile = Join-Path $scratchDir "temp_prod.json"
    $json | Out-File -FilePath $tempFile -Encoding utf8

    $response = curl.exe -s -X POST "$baseUrl/products" `
        -H "Accept: application/json" `
        -F "dto=@$tempFile;type=application/json" `
        -F "files=@$imagePath"

    Write-Host "Response: $response"
    
    $resObj = $response | ConvertFrom-Json
    
    Remove-Item $tempFile
    Write-Host "Created Product [$($prod.name)] with ID: $($resObj.id)"
}

Write-Host "Data Population Completed Successfully!"
