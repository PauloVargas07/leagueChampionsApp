describe('My Login application', () => {
    it('should login with valid credentials', async () => {

        await browser.pause(5990);

        const championCardDetail = await $(`//android.widget.TextView[@text='Aatrox']`);
        await championCardDetail.waitForDisplayed({ timeout: 5000 });
        await championCardDetail.click();

        const championSound = await $('~Play Sound');
        await championSound.waitForDisplayed({ timeout: 5000 });
        await championSound.click();

        await browser.pause(12000);
    })
})

