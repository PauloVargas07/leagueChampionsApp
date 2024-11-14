describe('Open Champion Card', () => {
    it('should login with valid credentials', async () => {

        const championCardDetail = await $(`//android.widget.TextView[@text='Aatrox']`);
        await championCardDetail.waitForDisplayed({ timeout: 5000 });
        await championCardDetail.click();

        const championSound = await $('~Play Sound');
        await championSound.waitForDisplayed({ timeout: 5000 });
        await championSound.click();

        await browser.pause(6000);
        const backButton = await $('~Back');
        await backButton.click();
        await browser.pause(4000);
    })

    it('should sort teams and share', async () => {
        const sortTeams = await $(`//android.widget.TextView[@text='Sort Teams']`);
        await sortTeams.waitForDisplayed({ timeout: 5000 });
        await sortTeams.click();
        await browser.pause(500);
        await sortTeams.click();

        await browser.pause(4000);

        const allowButton = await $(`//android.widget.Button[@text='Allow']`);
        await allowButton.waitForDisplayed({ timeout: 5000 });
        await allowButton.click();

        await browser.pause(4000);

        const shareTeamsButton = await $(`//android.widget.TextView[@text='Share Teams']`);
        await shareTeamsButton.waitForDisplayed({ timeout: 5000 });
        await shareTeamsButton.click();

        await browser.pause(4000);

        await browser.back();

        // const shareTeamsButton = await $(`//android.widget.TextView[@text='Share Teams']`);
        // await shareTeamsButton.waitForDisplayed({ timeout: 5000 });
        // await shareTeamsButton.click();

        const backButton = await $(`//android.widget.ImageView[contains(@resource-id, 'icon_id')]`);
        await backButton.waitForDisplayed({ timeout: 5000 });
        await backButton.click();
    })

    // it('items', async () => {
    //
    // })

})



