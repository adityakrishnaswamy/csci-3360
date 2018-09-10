names(dataset1)
model <- lm(WPI~year+Money_printed+GDP+Interest_RATE, data=dataset1)
print(model)